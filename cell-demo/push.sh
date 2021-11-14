#!/bin/bash

function existIfFail() {
    if [[ $? -ne 0 ]]; then
        exit -1
    fi
}
docker login

imgs=(demo demorpc prometheus gateway)

for name in ${imgs}
do
    docker tag jokerlvccc/cell_${name}:0.0.1 jokerlvccc/cell_${name}:0.0.1
    existIfFail
    docker push jokerlvccc/cell_${name}:0.0.1
    existIfFail
done