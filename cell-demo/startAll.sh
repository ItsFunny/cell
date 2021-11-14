#!/bin/bash
cd ../
mvn clean install -Dmaven.test.skip=true
./build.sh 1
./run.sh