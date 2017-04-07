# CUDL Viewer Database Setup

Follow these steps to setup a new database for the CUDL Viewer.

## Create a Postgres database

```ShellSession
$ createdb my-cudl-db
```

## Create Tables

```ShellSession
$ psql -e -1 -f src/main/docs/database-setup.psql my-cudl-db
```

* `-e` echoes all SQL commands executed
* `-1` wraps the executed commands in a transaction so that nothing changes
  if the script fails
* `-f` executes commands from the provided SQL file

### Apply migrations

Apply all the migration scripts in `src/main/docs/db-migrations/`:

```ShellSession
$ find src/main/docs/db-migrations -name '*.psql' \
    -exec psql -e -1 -f {} my-cudl-db \;
```

## Import existing data

Clone the [database snapshots repository](https://bitbucket.org/CUDL/snapshots)
from bitbucket:

```ShellSession
$ git clone git@bitbucket.org:CUDL/snapshots.git
```

The repo contains data for three tables: `collections`, `items` and
`itemsincollection` under `databases/cudl-viewer-partial-dump`:

```ShellSession
$ tree
.
└── databases
    ├── copy.psql
    ├── cudl_db_live.sql
    └── cudl-viewer-partial-dump
        ├── collections
        ├── items
        ├── itemsincollection
        └── readme.txt
```

The data is created from SQL `COPY` commands:

```PLpgSQL
\COPY collections FROM './cudl-viewer-partial-dump/collections'
\COPY items FROM './cudl-viewer-partial-dump/items'
\COPY itemsincollection FROM './cudl-viewer-partial-dump/itemsincollection'
```

`copy.psql` in the snapshots repository contains these commands, so you can
perform the copy as follows:
```ShellSession
$ psql -e -1 -f copy.psql my-cudl-db
```


## Creating admin users

To create an admin user, first have the user log in to create a user record
in the database.

Once the user is created, the authority value `ROLE_ADMIN` must be set in the
`authorities` table:

```PLpgSQL
my-cudl-db=# CREATE EXTENSION pgcrypto;
my-cudl-db=# INSERT INTO authorities (username, authority) VALUES ('raven:' || encode(digest('hwtb2', 'sha256'), 'hex'), 'ROLE_ADMIN');
```

And the user's name and email needs to be added to the `adminusers` table:

```PLpgSQL
my-cudl-db=# INSERT INTO adminusers (username, name, email) VALUES ('raven:' || encode(digest('hwtb2', 'sha256'), 'hex'), 'Hal Blackburn', 'hwtb2@cam.ac.uk');
```

In the examples above, the username format is for raven. The format is
`raven:<hash>` where `<hash>` is the hex-encoded sha256 sum of the user's CRSID.
Login methods other than Raven will have different formats.
