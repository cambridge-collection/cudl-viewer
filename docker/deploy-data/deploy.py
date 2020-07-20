#!/usr/bin/env python3

import json
import ntpath
import os
import sys
import git
import psycopg2
import shutil
import glob
from distutils.dir_util import copy_tree
import configparser
import requests


# Write docker-compose to ensure python and libs installed, script is copied and to set cron job

# title is database instance name, e.g. dev, staging, live.
# git_data_repo is place to checkout releases repo
# dbdeploy is database info for deploy db
# dbdl is cudl database used for updating collections data
#
# NOTE: Requires ssh key setup with access to read repo

def main():
    config = configparser.ConfigParser()
    config.read('/usr/local/deploy-data/config.ini')

    title = config['DEPLOY']['INSTANCE_NAME']
    git_data_repo_dir = config['DEPLOY']['GIT_DATA_REPO_DIR']
    git_data_repo_url = config['DEPLOY']['GIT_DATA_REPO_URL']
    db_deploy_host = config['DEPLOY']['DB_HOST']
    db_deploy_port = config['DEPLOY']['DB_PORT']
    db_deploy_database = config['DEPLOY']['DB_DATABASE']
    db_deploy_user = config['DEPLOY']['DB_USER']
    db_deploy_passwd = config['DEPLOY']['DB_PASSWORD']
    db_dl_host = config['CUDL']['DB_HOST']
    db_dl_port = config['CUDL']['DB_PORT']
    db_dl_database = config['CUDL']['DB_DATABASE']
    db_dl_user = config['CUDL']['DB_USER']
    db_dl_passwd = config['CUDL']['DB_PASSWORD']
    content_dir = config['CUDL']['CONTENT_DIR']
    data_dir = config['CUDL']['DATA_DIR']
    refresh_url = config['CUDL']['REFRESH_URL']

    required_version = _get_required_version_from_db(title, db_deploy_database, db_deploy_user, db_deploy_host,
                                                     db_deploy_passwd, db_deploy_port)

    print(required_version)
    # If git_data_repo_dir does not exist or is empty clone repo, create dir and git clone
    if not os.path.isdir(git_data_repo_dir):
        os.makedirs(git_data_repo_dir)
    if len(os.listdir(git_data_repo_dir)) == 0:
        git.Repo.clone_from(git_data_repo_url, git_data_repo_dir)

    # Check out from git (NOTE: Requires ssh key setup with access to read repo)
    repo = git.Repo(git_data_repo_dir)
    ssh_cmd = 'ssh -i /root/.ssh/id_rsa'

    with repo.git.custom_environment(GIT_SSH_COMMAND=ssh_cmd):
        repo.remotes.origin.fetch()

        old_tag = repo.head.object.hexsha
        # set to specific tag
        repo.git.checkout(required_version)
        new_tag = repo.head.object.hexsha
        version_changed = (new_tag != old_tag) or not os.listdir(content_dir) or not os.listdir(data_dir)

    if version_changed:

        print("Detected updated data, deploying...")

        # Update collections db
        collections = _get_collections_from_dataset(git_data_repo_dir)
        collections_json = []

        for index, collection in enumerate(collections):
            # open file for this collection
            with open(os.path.join(git_data_repo_dir, collection['@id'])) as collection_file:
                print(collection_file)
                collections_json.append(json.load(collection_file))

        _update_collections_in_db(db_dl_database, db_dl_user, db_dl_host, db_dl_passwd, collections_json, db_dl_port)

        # Copy data from pages dir into content dir
        if os.path.isdir(content_dir):
            files = glob.glob(content_dir + '*')
            for f in files:
                if f == content_dir:
                    continue
                if os.path.isdir(f):
                    shutil.rmtree(f)
                else:
                    os.remove(f)
        copy_tree(git_data_repo_dir + os.sep + "pages", content_dir)

        # Copy data from items dir into data
        if os.path.isdir(data_dir):
            files = glob.glob(data_dir + '*')
            for f in files:
                if f == data_dir:
                    continue
                if os.path.isdir(f):
                    shutil.rmtree(f)
                else:
                    os.remove(f)
        copy_tree(git_data_repo_dir + os.sep + "items" + os.sep + "json", data_dir + os.sep + "json")
        copy_tree(git_data_repo_dir + os.sep + "items" + os.sep + "tei",
                  data_dir + os.sep + "data" + os.sep + "tei")

        # Write out current version to data folder.
        version_path = data_dir + os.sep + "_current_data_version"
        version_file = open(version_path, 'w')
        version_file.write("{ tag: " + required_version + ", hexsha:" + new_tag + " }")
        version_file.close()

        _refresh_viewer_cache(refresh_url)
        print("Done.")


# Get the current version in the database
def _get_required_version_from_db(title, deploydatabase, dbdeployuser, dbdeployhost, dbdeploypasswd, dbdeployport):
    conn = None
    try:
        conn = psycopg2.connect(
            "dbname = '" + deploydatabase + "' user = '" + dbdeployuser +
            "' host = '" + dbdeployhost + "' password = '" + dbdeploypasswd +
            "' port = " + dbdeployport
        )

        curs = conn.cursor()
        curs.execute("SELECT version from currentversions where instanceid=%s;", (title,))
        row = curs.fetchone()  # should just be one version per instanceid

        return row[0]

    except psycopg2.DatabaseError as ex:
        print("Error connecting to the DB: " + str(ex))
        conn.close()
        sys.exit(1)

    finally:
        if conn is not None:
            conn.close()


# Find first json file in root directory that implements 'https://schemas.cudl.lib.cam.ac.uk/package/v1/dl-dataset.json'
def _get_collections_from_dataset(git_data_repo):
    json_files = [pos_json for pos_json in os.listdir(git_data_repo) if pos_json.endswith('.json')]
    for js in json_files:
        with open(os.path.join(git_data_repo, js)) as dataset_file:
            collection_text = json.load(dataset_file)
            if collection_text["@type"] == "https://schemas.cudl.lib.cam.ac.uk/package/v1/dl-dataset.json":
                # This is the dataset file, so read in the collections
                collections = collection_text['collections']
                dataset_file.close()
                return collections


def _update_collections_in_db(dldatabase, dbdluser, dbdlhost, dbdlpasswd, collections_json, dbdlport):
    conn = None
    try:
        conn = psycopg2.connect(
            "dbname = '" + dldatabase + "' user = '" + dbdluser +
            "' host = '" + dbdlhost + "' password = '" + dbdlpasswd + "'"
            + " port = " + dbdlport)

        curs = conn.cursor()

        curs.execute("DELETE FROM itemsincollection")
        curs.execute("DELETE FROM collections")
        curs.execute("DELETE FROM items")

        # Add collections
        for index, collection_json in enumerate(collections_json):

            # Read in the information from the JSON we need.
            collectionid = collection_json["name"]["url-slug"]
            collectiontitle = collection_json["name"]["full"]
            summaryurl = collection_json["description"]["full"]["@id"]
            sponsorsurl = collection_json["credit"]["prose"]["@id"]
            collectiontype = "organisation"  # TODO
            collectionorder = index + 1
            parentcollectionid = None  # TODO
            metadescription = collection_json["description"]["medium"]
            items = collection_json["items"]

            # translate summary and sponsors urls
            summaryurl = summaryurl.replace('pages/html/', '')  # TODO make this nicer.
            sponsorsurl = sponsorsurl.replace('pages/html/', '')  # TODO make this nicer.

            curs.execute(
                "INSERT INTO collections (collectionid,title,summaryurl,sponsorsurl,type,collectionorder,parentcollectionid,metadescription) "
                + "VALUES (%s, %s, %s, %s, %s, %s, %s, %s) ",
                (collectionid, collectiontitle, summaryurl, sponsorsurl, collectiontype, collectionorder,
                 parentcollectionid,
                 metadescription))

            # Add items
            for item_index, item in enumerate(items):
                # get the itemid from the file name
                itemid = ntpath.basename(os.path.splitext(item['@id'])[0])

                # check if item exists already (as it can be added from another collection)
                curs.execute("SELECT itemid from items where itemid = %s", (itemid,))
                if curs.fetchone() is None:
                    curs.execute("INSERT INTO items (itemid, taggingstatus, iiifenabled) VALUES (%s, %s, %s)",
                                 (itemid, False, True))  # TODO taggingstatus and iiifenabled hardcoded for now.

                curs.execute("INSERT INTO itemsincollection (itemid, collectionid, visible, itemorder) "
                             "VALUES (%s, %s, %s, %s)", (itemid, collectionid,
                                                         True,
                                                         item_index + 1))

        conn.commit()

    except psycopg2.DatabaseError as ex:
        print("Error connecting to the DB: " + str(ex))
        conn.close()
        sys.exit(1)
    finally:
        if conn is not None:
            conn.close()


def _refresh_viewer_cache(refresh_url):
    url = refresh_url
    response = requests.get(url)
    print(response)


if __name__ == '__main__':
    main()
