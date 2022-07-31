#!/bin/bash
echo "Can use .env file if you want"
# source ./.env
docker-compose up -d --build
# docker port $HTTP_SERVER_DOCKER_CONTAINER_NAME
docker port http_server
