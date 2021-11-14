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




ARG APPLICATION_OPTIONS="-path /Users/joker/Java/config -type Default"
ENV ENV_APPLICATION_OPTIONS=$APPLICATION_OPTIONS

# http
EXPOSE 8888


CMD ["sh","-c","java -server ${ENV_VM_OPTIONS} -Dloader.path=/Users/joker/Java/lib-thrid -jar ./web/execute.jar ${ENV_APPLICATION_OPTIONS}"]