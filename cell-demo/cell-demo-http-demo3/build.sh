#!/bin/bash

mvn clean install -Dmaven.test.skip=true

imageName=demo

docker rmi -f $(docker images --filter=reference='jokerlvccc/cell_${imageName}*' -qa)
docker build --no-cache -t jokerlvccc/cell_${imageName}:0.0.1 .

