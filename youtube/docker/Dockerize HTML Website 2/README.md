## [Coursera Guided Project: Introduction to Docker Build Portfolio Site](https://www.coursera.org/projects/introduction-to-docker-build-portfolio-site)

## Docker Portfolio Website Example

Simple example of a dockerized portfolio website

## Quick Start

```bash
# Run in Docker

pull nginx image

docker run -d nginx

docker exec -it CONTAINER_ID bash

# mapping docker image port
docker run -d -p8080:80 nginx (8080 is the localmachine port, 80 is the docker image port)

# link present working directory to docker image

cd website

docker run -d -p8081:80 --name website -v $(pwd):/website nginx

docker run -d -p8080:80 --name website -v $(pwd):/usr/share/nginx/html nginx

chmod -R o+rX <path to directory where website files are at>



```
