#!/bin/bash
# source ../.env

CURRENT_DIRECTORY=$(pwd)

docker build \
--file=http_server.Dockerfile \
--tag="http_server:0.1" \
.
# --build-arg HTTP_PORT=$HTTP_SERVER_HTTP_PORT \
# $CURRENT_DIRECTORY

docker create \
--interactive \
--tty \
--rm \
--name=http_server \
--publish-all \
--mount type=bind,source="$(pwd)/src",target=/home/http_server_user/src \
--workdir /home/http_server_user/src \
http_server:0.1

docker start http_server
docker port http_server
docker attach http_server

# chmod +x http_server.py
# ./http_server.py
