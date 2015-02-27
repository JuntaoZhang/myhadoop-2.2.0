#!/bin/bash
source my.conf

cd ${MYHADOOP_SRC_HOME}

clush -g hadoop -c ${MYHADOOP_SRC_TARGET_HOME}/test  --dest=~/hadoop/etc/hadoop/