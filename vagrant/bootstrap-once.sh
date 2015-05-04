#!/bin/bash

echo "updating packages..."
apt-get update -qq

echo "installing Java runtime..."
apt-get install openjdk-7-jre-headless -y -qq

echo "downloading OrientDB..."
wget http://orientdb.com/download.php?file=orientdb-community-2.0.8.tar.gz -q -O orientdb-community-2.0.8.tar.gz

