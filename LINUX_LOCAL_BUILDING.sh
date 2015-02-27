#!/bin/bash
MYHADOOP_SRC_HOME="/root/hadoop/myhadoop-2.2.0-src"
MYHADOOP_SRC_TARGET_HOME=${MYHADOOP_SRC_HOME}"/hadoop-dist/target/hadoop-2.2.0"

cd ${MYHADOOP_SRC_HOME}

export MAVEN_OPTS="-Xms256m -Xmx512m"
mvn package -Pdist,native -DskipTests -o

cd ${MYHADOOP_SRC_TARGET_HOME}
rm -rf bin/*.cmd
rm -rf sbin/*.cmd
cp -u ${MYHADOOP_SRC_HOME}/conf/* ${MYHADOOP_SRC_TARGET_HOME}/etc/hadoop/

