#!/bin/bash
if [[ -r ${PWD}/target ]];then
rm -rf ${PWD}/target
fi
mvn clean install -Dmaven.test.skip=true

imageName=gateway

docker rmi -f $(docker images --filter=reference='cell/${imageName}*' -qa)
docker build --no-cache -t cell/${imageName}:0.0.1 .


