# CUDL Viewer

The CUDL Viewer is a [Spring](https://spring.io/)-powered java web application.

The Viewer uses [Maven](https://maven.apache.org/) for dependency management
of 3rd party Java libraries, and build automation.

Ensure you have a recent JDK and version of Maven installed before continuing.

## Building

To build a WAR file, run:

```
$ mvn clean package
```

The war file will be created under `target/`.

When deployed, the Viewer requires `cudl-global.properties` to exist on the
classpath.

This file will be excluded from any WAR file generated as it contains the properties
that vary between systems (DEV, BETA, LIVE etc). This file should be copied into the
classpath for your web container (e.g. `lib` directory in Tomcat).

## Run locally

### Using sample data

The sample data is linked as a git submodule so we need to initalise
it and download the data.  Do this with the following commands:

    git submodule init
    git submodule update

Check the git submodules are present: dl-data-samples should be at:

    docker/db/dl-data-samples

To run the viewer:

    docker-compose --env-file sample-data.env up

When running you can then access the Viewer at
[http://localhost:8888/](http://localhost:8888/).


### Using cudl data

Check the cudl database snapshots are available here:
(NOTE: These are held in Bitbucket and are available to Cambridge developers)

    git submodule init docker/db/snapshots

Also checkout the cambridge processed data from Bitbucket into the parent dir:

    cd ..
    git clone git@bitbucket.org:CUDL/cudl-data-releases.git cudl-data-releases

To run the viewer:

    docker-compose --env-file cudl-data.env up

Note: if switching between data sets you may have to remove the old cudl-db images.
e.g.

    docker image rm cudl-viewer-master_cudl-db:latest

### Required Configuration

The Viewer is configured in the `cudl-global.properties` file. A template is
available at `docker/cudl-global.properties`.

Go through the file, updating defaults as desired. Most of the defaults are
acceptable to get the Viewer running, but some must be changed:

* `cudl-viewer-content.html.path`
* `cudl-viewer-content.images.path`

These must point to the `html/` and `images/` directories in a local checkout of
the data you're using (see above for details).


## Development

It uses the separate repository cudl-viewer-ui for all the javascript and css dependencies,
so this is a good thing to look at if you want to start customising your viewer instance.

### Live updates from `cudl-viewer-ui`

All the Javascript, CSS etc used by the viewer comes from `cudl-viewer-ui`
via a Maven dependency. You can make live changes to the UI Javascript etc
without rebuilding each time by using the UI's devserver.

This requires setting the `cudl.ui.dev` property to `true` in
`cudl-global.properties`. You can also set `cudl.ui.dev.baseUrl` if the default
of `http://localhost:8080/` is not right for you.

Once enabled, the viewer will link to the UI's devserver instead of serving
Javascript/CSS from the dependency JARs.

See the [cudl-viewer-ui's README](https://github.com/cambridge-collection/cudl-viewer-ui) for
instructions on running the UI devserver.


## Making a release

Releasing is using the Maven release plugin.

### Tag format

Releases are tagged using the old CARET Ops tag format, which is
`<server-class>-<date-yyyymmdd><release-in-day>`. `<release-in-day>` is a two
digit number, starting from `00` which increments after each release in one day.

For example, for the 1st release on `21/06/2015` for a production deployment
we'd use `production-2015062100`.

Note that the last two digits make tags a pain to auto-generate, so you'll have
to manually specify the tag value each time you tag.

### Step 1: Prepare

After committing and testing all changes, switch to the `main` branch and
run:

```
$ mvn release:prepare
```

You'll be prompted for the version and tag to use for the release, and the
version to use for the next version. Use a tag in the above format for the first
two. The third must be
`1.0-SNAPSHOT`.

After this finishes, you'll have two new commits on your `main` branch and
a new tag in your local repo. You need to push all of these to the CUDL repo.
Assuming your CUDL remote is `cudl`, and your created tag was
`production-2022062100`, you'd run:

```
$ git push cudl main production-2022062100
```

### Step 2: Perform

Once the release has been tagged you can finish off by deploying the artifacts
to the CUDL packages repository in GitHub using the command:

```
$ mvn release:perform
```

### Step 3: CAMBRIDGE ONLY: deploy to Maven repo on S3

Once the release has been tagged you can upload this version to our internal s3 repo
which is where the code is deployed from using Puppet.

You first need to log into AWS console (account cul-main) and ensure you have a user
in the group "CUDL-MAVEN-RELEASES-s3".  This will ensure that you have permission to write
to our s3 maven repository.

Once you have an account you should create an access key for it in the section
IAM -> <your account> -> Security Credentials -> Access Keys.

Then ensure that you have the aws credentials in your ~/.m2/settings.xml

```
  <servers>
	<server>
	<id>github</id>
	<username>YOUR_GITHUB_USERNAME</username>
	<password>YOUR_GITHUB_ACCESS_TOKEN></password>
	</server>
	<server>
	<id>cudl-aws-release</id>
	<username>YOUR_AWS_ACCESS_TOKEN_NAME</username>
	<password>YOUR_AWS_ACCESS_TOKEN_VALUE</password>
	</server>
  </servers>
```

After, we need to delete the tag (local only) so that we can run perform again
to publish the code to another repository.  You then need to input the same information
again (same tag name etc).

```
git tag --delete <TAG NAME>
mvn release:prepare -DpushChanges=false
```

Then run the following command to upload to the Cambridge s3 repository for release
so that puppet can find and deploy the code.

```
mvn release:stage -DstagingRepository=cudl-aws-release::default::s3://mvn.cudl.lib.cam.ac.uk/release
```

If it runs correctly, you should have two new commits that are duplicates of the ones you made when building the release for GitHub. One of them should also have the tag you defined above. All that remains is to delete that tag and reset those two commits.

```
git tag --delete <TAG NAME>
git reset HEAD~2
```

Once you have confirmed that those two commits have been removed, don't forget to `git pull`. This will restore the original tag you deleted, as well as bring over any other changes that might have been pushed by others to `main`.

Now you can update the development puppet to pull in the new version.
See https://gitlab.developers.cam.ac.uk/lib/dev/dev-puppet/

## More information

For more information, see the [Documentation](https://cambridge-collection.github.io).
