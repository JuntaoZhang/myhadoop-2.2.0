#!/bin/sh
rm /Users/juntaozhang/Workspace/hadoop/hadoop/hadoop-dist/target/hadoop-2.2.0/etc/hadoop/core-site.xml*
rm /Users/juntaozhang/Workspace/hadoop/hadoop/hadoop-dist/target/hadoop-2.2.0/etc/hadoop/hdfs-site.xml*

cp /Users/juntaozhang/Workspace/hadoop/hadoop/hadoop-test/src/main/resources/core-site.xml /Users/juntaozhang/Workspace/hadoop/hadoop/hadoop-dist/target/hadoop-2.2.0/etc/hadoop/
sed -i ".bak" "s/\/home\/hadoop\/data/\/Users\/Shared\/hadoop/g" /Users/juntaozhang/Workspace/hadoop/hadoop/hadoop-dist/target/hadoop-2.2.0/etc/hadoop/core-site.xml

cp /Users/juntaozhang/Workspace/hadoop/hadoop/hadoop-test/src/main/resources/hdfs-site.xml /Users/juntaozhang/Workspace/hadoop/hadoop/hadoop-dist/target/hadoop-2.2.0/etc/hadoop/
sed -i ".bak" "s/\/home\/hadoop\/data/\/Users\/Shared\/hadoop/g" /Users/juntaozhang/Workspace/hadoop/hadoop/hadoop-dist/target/hadoop-2.2.0/etc/hadoop/hdfs-site.xml


hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar pi 3 3
