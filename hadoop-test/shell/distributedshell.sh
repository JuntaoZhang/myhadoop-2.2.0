#!/bin/bash
cd ~/
#DS_OPTS=
export YARN_LOGFILE="yarn-distributedshell-client.log"
yarn jar hadoop/mylib/hadoop-test-*.jar \
    org.example.hadoop.yarn.applications.distributedshell.Client \
    --jar hadoop/mylib/hadoop-test-*.jar \
    --shell_command ls /home/hadoop/ \
    --num_containers 3 \
    --master_memory 64 \
    --priority 10 \
    --debug