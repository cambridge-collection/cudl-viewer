#!/bin/bash
#
# CUDL Viewer entrypoint script, used to access properties from S3

s3get() {
  set -eu;
  aws --version;
  echo "Getting properties from $S3_URL";
  aws s3 cp "$S3_URL" /etc/cudl-viewer/cudl-global.properties
}

s3get
exec "$@"
