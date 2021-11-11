#!/bin/bash

mvn clean install -Dmaven.test.skip=true

docker build --no-cache -t cell/demo:0.0.1 .

