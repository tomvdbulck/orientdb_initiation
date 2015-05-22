#!/bin/bash

echo "updating packages..."
apt-get update -qq

echo "installing Java runtime..."
apt-get install openjdk-7-jre-headless -y -qq > /dev/null

echo "downloading OrientDB..."
wget http://orientdb.com/download.php?file=orientdb-community-2.0.8.tar.gz -q -O orientdb-community-2.0.8.tar.gz

echo "setting up OrientDB..."
tar xf orientdb-community-2.0.8.tar.gz
ln -s orientdb-community-2.0.8/ orientdb
cd orientdb/

sed -i '/<\/users>/i \
	<user name="root" password="ordina" resources="*"></user>' config/orientdb-server-config.xml
sed -i "s|YOUR_ORIENTDB_INSTALLATION_PATH|/home/vagrant/orientdb|;s|admin|vagrant|" bin/orientdb.sh

chown -R vagrant.vagrant .
