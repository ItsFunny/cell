#!/bin/bash

# start nacos
docker-compose -f docker-compose-nacos.yaml  down --remove-orphans && \
docker-compose -f docker-compose-nacos.yaml up -d && \
docker-compose -f docker-compose-prometheus.yaml up -d


docker ps | grep 'cell' | awk -F 'cell' '{print $1}' | awk -F 'jokerlvccc'  '{print $1}'  | xargs docker rm -f

echo "start prometheus discovery"
docker run -p 9999:8080  -it --name 9999 -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e LOG_LEVEL=INFO -e TYPE=Default -v /Users/joker/Java/cell/cell-demo/cell-demo-http-demo3/config:/Users/joker/Java/config jokerlvccc/cell_prometheus:0.0.1

echo "start rpc"
docker run -p 8081:8080  -p 7001:7000 -it --name 7001 -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e LOG_LEVEL=INFO -e TYPE=Default -v /Users/joker/Java/cell/cell-demo/cell-demo-http-demo3/config:/Users/joker/Java/config jokerlvccc/cell_demorpc:0.0.1
echo "start  http"
docker run -p 8080:8080  -p 7000:7000 -it --name 8080 -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e LOG_LEVEL=INFO -e TYPE=Default -v /Users/joker/Java/cell/cell-demo/cell-demo-http-demo3/config:/Users/joker/Java/config jokerlvccc/cell_demo:0.0.1
echo "start gateway"
docker run -p 8888:8080  -it --name 8888 -e ENV_VM_OPTIONS="-Xmx1g -Xms1g " -e LOG_LEVEL=INFO -e TYPE=Default -v /Users/joker/Java/cell/cell-demo/cell-demo-http-demo3/config:/Users/joker/Java/config jokerlvccc/cell_gateway:0.0.1
