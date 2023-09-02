#!/bin/bash
mvn clean package deploy

pwd=${PWD}
cd ../maven_repository/maven-repo/com/cell/
rm -rf cell-runnable*
rm -rf cell-plugin-develop
rm -rf cell-self-blockchain
git add .
git commit -m "update"
git push origin master -f
cd ${PWD}