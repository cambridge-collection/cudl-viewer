#!/bin/sh
echo "copying inital data..."

/usr/local/deploy-data/deploy.py

echo "starting cron..."
cron -f
