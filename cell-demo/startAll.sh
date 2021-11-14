#!/bin/bash
path=${PWD}
cd ../
mvn clean install -Dmaven.test.skip=true
cd ${path}
./build.sh 1
./run.sh