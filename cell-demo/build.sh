#!/bin/bash

docker ps | grep 'cell' | awk -F 'cell' '{print $1}' | awk -F 'jokerlvccc'  '{print $1}'  | xargs docker rm -f

files=(cell-demo-http-demo3 cell-demo-rpc)
als=(demo demorpc)
l=`expr ${#fiels[*]} + 1 `
for i  in $(seq 0 ${l})
do
    file=${files[i]}
    if [[ -r ${PWD}/target ]];then
        rm -rf ${PWD}/target
    fi
    echo "开始打包:${file}"
    mvn clean install -Dmaven.test.skip=true  -T4 -pl  ${file} -am
    if [[ $? -ne 0 ]];then
            echo "打包失败  mvn clean install -Dmaven.test.skip=true  -T4 -pl  ${file} -am"
            exit -1
    fi
    imageName=${als[i]}
    echo "开始打包 ,镜像 :${imageName}"
    docker rmi -f $(docker images --filter=reference='jokerlvccc/cell_${imageName}*' -qa)
    docker build --no-cache -t jokerlvccc/cell_${imageName}:0.0.1 .
done

