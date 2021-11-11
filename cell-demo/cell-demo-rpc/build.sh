#!/bin/bash
if [[ -r ${PWD}/target ]];then
rm -rf ${PWD}/target
fi
mvn clean install -Dmaven.test.skip=true

imageName=demorpc

docker rmi -f $(docker images --filter=reference='jokerlvccc/cell_${imageName}*' -qa)
docker build --no-cache -t jokerlvccc/cell_${imageName}:0.0.1 .


