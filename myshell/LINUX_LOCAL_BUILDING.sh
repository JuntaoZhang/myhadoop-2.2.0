#!/bin/bash
source my.conf

cd ${MYHADOOP_SRC_HOME}

export MAVEN_OPTS="-Xms256m -Xmx512m"
mvn package -Pdist,native -DskipTests -o

cd ${MYHADOOP_SRC_TARGET_HOME}
rm -rf bin/*.cmd
rm -rf sbin/*.cmd
cp -u ${MYHADOOP_SRC_HOME}/conf/* ${MYHADOOP_SRC_TARGET_HOME}/etc/hadoop/



