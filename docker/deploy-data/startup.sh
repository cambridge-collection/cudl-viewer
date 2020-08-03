#!/bin/sh
echo "copying inital data..."

printenv | grep -v "no_proxy" >> /etc/environment

/usr/local/deploy-data/deploy.py

echo "starting cron..."
cron -f
