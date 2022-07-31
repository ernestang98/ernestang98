## Docker Tutorial Example

Simple example of how to execute docker commands

## Quick Start

```bash
# Run in Docker (Run creates a docker container)
sudo docker run --name MyUbuntu ubuntu:latest

# Run in Docker and interactive container instance
sudo docker run -it --name MyUbuntu1 ubuntu:latest

# List all running containers
sudo docker ps -a

# Attach i/o of container with terminal 
sudo docker attach MyUbuntu1

# Print logs (add running logs use -f)
docker logs CONTAINER_ID
docker logs -f CONTAINER_ID

# Inspecting Docker Containers
docker inspect CONTAINER_ID

# Cleaning up files (important)
docker system prune

# Execute "pwd" in container MyUbuntu1
sudo docker exec MyUbuntu1 pwd
sudo docker exec MyUbuntu1 echo $PATH

# Removing docker resources
sudo docker rm -f MyUbuntu MyUbuntu1

# Creating custom ubuntu image with nginx
sudo docker run -it --name MyNginx ubuntu:latest
apt-get update
apt-get install nginx
which nginx 
apt-get install curl
service nginx start
curl http://localhost:80
sudo docker commit MyNginx userid/repo
sudo docker push MyNginx userid/repo
sudo docker pull MyNginx userid/repo
sudo docker run -it --name MySpecNginx userid/repo

# Creating custom ubuntu image with nginx part 2
sudo docker run -d --name MyApp nginx
sudo docker exec -it MyApp bash
<change the html file, commit and push>
```

## Additional Docker things

[Data persistence in docker containers](https://stackoverflow.com/questions/28574433/do-docker-containers-retain-file-changes)