#!/bin/bash
mvn clean package deploy

pwd=${PWD}
cd ../maven_repository/maven-repo/com/cell/
rm -rf cell-runnable*
git add .
git commit -m "update"
git push origin master -f
cd ${PWD}