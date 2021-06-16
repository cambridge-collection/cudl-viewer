FROM postgres:12.1-alpine

ARG DBPATH

# Postgres executes *.sql files in /docker-entrypoint-initdb.d/ - place the
# migration SQL there, plus an SQL script to load the dumped data.
COPY ./src/main/resources/db/migration/ /docker-entrypoint-initdb.d/
# Prefix migration SQL files with 0_ so they always run first
RUN find /docker-entrypoint-initdb.d/ -type f -exec sh -c 'mv "{}" "$(dirname "{}")/0_$(basename "{}")"' ';'
COPY ./docker/db/copy-snapshot-data.sql /docker-entrypoint-initdb.d/1_copy-snapshot-data.sql

# copy data
COPY ${DBPATH}/databases/cudl-viewer-partial-dump/items /srv/cudl-viewer/db-dump/items.txt
COPY ${DBPATH}/databases/cudl-viewer-partial-dump/collections /srv/cudl-viewer/db-dump/collections.txt
COPY ${DBPATH}/databases/cudl-viewer-partial-dump/itemsincollection /srv/cudl-viewer/db-dump/itemsincollection.txt

# sample data
#COPY ./docker/db/dl-data-samples/processed-data/database-snapshots/databases/cudl-viewer-partial-dump/items /srv/cudl-viewer/db-dump/items.txt
#COPY ./docker/db/dl-data-samples/processed-data/database-snapshots/databases/cudl-viewer-partial-dump/collections /srv/cudl-viewer/db-dump/collections.txt
#COPY ./docker/db/dl-data-samples/processed-data/database-snapshots/databases/cudl-viewer-partial-dump/itemsincollection /srv/cudl-viewer/db-dump/itemsincollection.txt


# overwrite with cudl data if present, comment out if not needed.
#COPY ./docker/db/snapshots/databases/cudl-viewer-partial-dump/collections /srv/cudl-viewer/db-dump/collections.txt
#COPY ./docker/db/snapshots/databases/cudl-viewer-partial-dump/items /srv/cudl-viewer/db-dump/items.txt
#COPY ./docker/db/snapshots/databases/cudl-viewer-partial-dump/itemsincollection /srv/cudl-viewer/db-dump/itemsincollection.txt
