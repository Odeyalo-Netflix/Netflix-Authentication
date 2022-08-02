#!/bin/bash

export MICROSERVICE_NAME=auth-service
export ACCESS_TOKEN=TOKEN
echo "Starting building image with name: $MICROSERVICE_NAME"

docker image build  -t $MICROSERVICE_NAME .


echo "Image $MICROSERVICE_NAME has been successfully built"


