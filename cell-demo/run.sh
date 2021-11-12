#!/bin/bash

# 1: 有多少个集群
# 2: 每个集群内有多少个rpc
# 3: 每个集群内有多少个http
# 默认为1 个集群 ,1个rpc ,1个http


rpcBeginPort=6000
httpBeginPort=8000
httpLocalRpcBeginPort=18000
gatewayBeginPort=9999
configPath=${PWD}/config
function cleanPreapre() {
   # start nacos
docker-compose -f docker-compose-nacos.yaml  down --remove-orphans
}

function cleanLogic() {
    docker ps -a | grep 'cell' | awk -F 'cell' '{print $1}' | awk -F 'jokerlvccc'  '{print $1}'  | xargs docker rm -f
}
function startPrepare() {
    echo "start nacos"
    docker-compose -f docker-compose-nacos.yaml up -d
    echo "start prometheus"
    docker-compose -f docker-compose-prometheus.yaml up -d
    echo "start prometheus discovery"
    docker run -p 9999:8080  -it --name 9999 -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e LOG_LEVEL=INFO -e TYPE=Default -v ${configPath}:/Users/joker/Java/config jokerlvccc/cell_prometheus:0.0.1
}
function startRpc() {
    cluster=${2}
    for i in $(seq 1 ${1});do
        grpcPort=`expr ${rpcBeginPort} + ${i} `
        rpcName="rpcServer${grpcPort}"
        docker run -p ${grpcPort}:7000  -itd --name ${grpcPort} -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e ENV_APPLICATION_OPTIONS="-cluster ${cluster}  -rpcName  ${rpcName}" -v ${configPath}:/Users/joker/Java/config jokerlvccc/cell_demorpc:0.0.1
    done
}
function startGateway() {
    index=${1}
    cluster=${2}
    gatewayBeginPort=`expr ${gatewayBeginPort} + 10000 `

    docker run -p ${gatewayBeginPort}:8080  -itd --name ${gatewayBeginPort} -e ENV_APPLICATION_OPTIONS="-cluster ${cluster}" -v ${configPath}:/Users/joker/Java/config jokerlvccc/cell_gateway:0.0.1
}
function startHttp() {
    cluster=${2}
    for i in $(seq 1 ${1});do
       httpPort=`expr ${httpBeginPort} + ${i} `
       grpcPort=`expr ${httpLocalRpcBeginPort} + ${i} `
       rpcName="appRpcServer${httpPort}"
       docker run -p ${httpPort}:8080  -p ${grpcPort}:7000 -itd --name ${httpPort} -e ENV_APPLICATION_OPTIONS="-cluster ${cluster}  -rpcName  ${rpcName}"  -v ${configPath}:/Users/joker/Java/config jokerlvccc/cell_demo:0.0.1
    done
}

function start() {
    cleanLogic
    cleanPreapre
#startPrepare
    limit=${1}
    for i in $(seq 1 ${limit});do
           cluster="cluster${i}"
           delta=`expr  10 \* ${i} `
           httpBeginPort=`expr ${httpBeginPort} + ${delta}`
           httpLocalRpcBeginPort=`expr ${httpLocalRpcBeginPort} + ${delta} `
           rpcBeginPort=`expr ${rpcBeginPort} + ${delta} `
           startRpc ${1} ${cluster}
           startHttp ${2} ${cluster}
           startGateway i ${cluster}
    done

}

clusterCount=1
rpcCount=1
httpCount=1
if [[ -n ${1} ]];then
clusterCount=${1}
fi

if [[ -n ${2} ]];then
rpcCount=${2}
fi

if [[ -n ${3} ]];then
httpCount=${3}
fi

start ${clusterCount} ${rpcCount} ${httpCount}
#docker run -p 7001:7000  -itd --name 7001 -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e ENV_APPLICATION_OPTIONS="-path /Users/joker/Java/config -cluster cluster1 -port 8080 -rpcName  rpcasd  -grpcPort 7000 -grpcAddr localhost  -type Default" -v /Users/joker/Java/cell/cell-demo/config:/Users/joker/Java/config jokerlvccc/cell_demorpc:0.0.1
# docker run -p 8081:8080  -p 7001:7000  -it --name 7001 -e ENV_RPC="rpcServer1" -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e LOG_LEVEL=INFO -e TYPE=Default -v /Users/joker/Java/cell/cell-demo/config:/Users/joker/Java/config jokerlvccc/cell_demorpc:0.0.1
#echo "start  http"
#docker run -p 8080:8080  -p 7000:7000 -it --name 8080 -e ENV_RPC="rpcName1" -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e LOG_LEVEL=INFO -e TYPE=Default -v /Users/joker/Java/cell/cell-demo/config:/Users/joker/Java/config jokerlvccc/cell_demo:0.0.1
#echo "start gateway"
#docker run -p 8888:8080  -it --name 8888 -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e LOG_LEVEL=INFO -e TYPE=Default -v /Users/joker/Java/cell/cell-demo/config:/Users/joker/Java/config jokerlvccc/cell_gateway:0.0.1

