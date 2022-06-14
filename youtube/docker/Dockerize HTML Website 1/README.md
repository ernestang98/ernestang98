## [Docker Essentials & Building a Containerized Web Application](https://www.coursera.org/projects/docker-container-essentials-web-app)

## 1. Docker Basics

docker run hello-world

docker pull busybox

docker run busybox

docker run busybox echo "hello world"

docker ps

docker ps -a

## 2. Docker Intermediate

docker run httpd

docker build . -t static-website

docker run -d -p 80:80 static-website

docker run -d -P static-website

## Docker Advanced

docker stats

docker logs

`./cadvisor.sh` 

- Launch Container Advisor monitoring tool
	
- [Link #1](https://github.com/google/cadvisor)
	
- [Link #2](https://prometheus.io/docs/guides/cadvisor/)

`./netdata.sh`

- Launch Netdata monitoring tool

- [Link #1](https://www.netdata.cloud/)