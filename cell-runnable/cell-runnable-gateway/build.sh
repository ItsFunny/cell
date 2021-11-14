#!/bin/bash
if [[ -r ${PWD}/target ]];then
rm -rf ${PWD}/target
fi

name=$(sh -c hostname)
if [[ ${name} -eq 'joker' ]];then
mvn clean install -Dmaven.test.skip=true --settings=/Users/joker/.m2/settings_back.xml
else
mvn clean install -Dmaven.test.skip=true
fi

imageName=gateway

docker rmi -f $(docker images --filter=reference='jokerlvccc/cell_${imageName}*' -qa)
docker build --no-cache -t jokerlvccc/cell_${imageName}:0.0.1 .


