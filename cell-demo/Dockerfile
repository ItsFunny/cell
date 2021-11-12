FROM jokerlvccc/java8:v1
MAINTAINER "jokerlvccc"

RUN mkdir -p /Users/joker/Java/web && mkdir -p /Users/joker/Java/config && chmod -R 777 /Users/joker
COPY ./target/execute.jar /Users/joker/Java/web/execute.jar

VOLUME /Users/joker/Java/config
VOLUME /Users/joker/Java/lib-thrid

WORKDIR /Users/joker/Java


ENV LANG C.UTF-8
ENV encoding UTF-8
ENV langmenu zh_CN.UTF-8
ENV fileencoding utf-8
ENV TYPE=Default
ENV LOG_LEVEL=INFO

ARG VM_OPTIONS="-Xmx1g -Xms1g"
ENV ENV_VM_OPTIONS=$VM_OPTIONS

ARG GRPC_ADDR="localhost"
ENV ENV_GRPC_ADDR=$GRPC_ADDR


ARG CLUSTER="DEFAULT_CLUSTER"
ENV ENV_CLUSTER=$CLUSTER

ARG RPC_NAME="rpcname"
ENV ENV_RPC=$RPC_NAME

ENV ENV_APPLICATION_OPTIONS="-rpcName  rpc -cluster DEFAULT_CLUSTER "

# http
EXPOSE 8080
# rpc
EXPOSE 7000


CMD ["sh","-c","java -server ${ENV_VM_OPTIONS} -Dloader.path=/Users/joker/Java/lib-thrid -jar ./web/execute.jar ${ENV_APPLICATION_OPTIONS} -path /Users/joker/Java/config  -port 8080 -grpcPort 7000 -grpcAddr localhost  -type Default "]