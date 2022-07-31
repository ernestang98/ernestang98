## [Coursera Guided Project: Containerization using Docker](https://www.coursera.org/projects/containerization-using-docker)

## Docker Basics

docker build . -t xxxx/yyyy

docker run -d -p 8085:5000 xxxx/yyyy

go to localhost:8085

## Docker Volumes

docker run -d --name myjenkins1 -p 50000:50000 -p 8087:8080 -v my-vol:/var/jenkins_home anjurose/jenkins

docker run -d --name myjenkins1 -p 50001:50000 -p 8089:8080 -v my-vol:/var/jenkins_home anjurose/jenkins

mkdir data

sudo docker cp myjenkins1:/var/jenkins_home ./data

username: admin

password: admin123

## Docker Save (Image)

docker save xxxx/yyyy > webapp.tar

docker load -i webapp.tar

## Docker Import Export (Container)

docker run -it --name myubuntu1 Ubuntu

apt-get update

apt-get nginx

docker export myubuntu1 > newsystem.tar

docker import - myubuntu1 < new system.tar

