#!/bin/sh
# wait-for-deploy-data

set -e

DIR="$1"
shift
cmd="$@"

until [ "$(ls -A $DIR)" ]; do
  >&2 echo "Data dir is empty - sleeping"
  sleep 1
done

>&2 echo "Data dir there - executing command"
exec $cmd

