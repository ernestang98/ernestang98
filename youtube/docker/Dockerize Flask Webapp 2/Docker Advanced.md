## Docker Network 

### Overview (containers & drivers)

https://docs.docker.com/network/

https://docs.docker.com/engine/tutorials/networkingcontainers/

One of the reasons Docker containers and services are so powerful is that you can connect them together, or connect them to non-Docker workloads. 

Docker containers and services do not even need to be aware that they are deployed on Docker, or whether their peers are also Docker workloads or not. 

Whether your Docker hosts run Linux, Windows, or a mix of the two, you can use Docker to manage them in a platform-agnostic way.

Run the below command to view your docker networks

```
docker network ls

NETWORK ID          NAME                DRIVER
18a2866682b8        none                null
c288470c46f6        host                host
7b369448dccb        bridge              bridge
```
By default, containers created always use bridge

Run to see what containers are connected to bridge

```
docker network inspect bridge
```
### Types of Network Drivers

- `bridge`: The default network driver. If you don’t specify a driver, this is the type of network you are creating. **Bridge networks are usually used when your applications run in standalone containers that need to communicate.** 

- `user-defined bridge`: You can also customize your own network driver, see [here](https://docs.docker.com/network/bridge/#manage-a-user-defined-bridge)

- `host`: For standalone containers, remove network isolation between the container and the Docker host, and use the host’s networking directly. (Shares host's networking, so exposing port 3000 on container == making it accessible on host's port 3000 i.e. to say localhost:3000 - usually need to link the port of container and the port of host e.g. -p3000:3000)

- `overlay`: Used more for Docker Swarm. Connect multiple Docker daemons (Daemon is the brain of a Docker host) together and enable swarm services to communicate with each other. You can also use overlay networks to facilitate communication between a swarm service and a standalone container, or between two standalone containers on different Docker daemons. This strategy removes the need to do OS-level routing between these containers.

- `macvlan`: Macvlan networks allow you to assign a MAC address to a container, making it appear as a physical device on your network. The Docker daemon routes traffic to containers by their MAC addresses.

- `none`: For this container, disable all networking. Usually used in conjunction with a custom network driver. `none` is not available for swarm services. See [disable container networking](https://docs.docker.com/network/none/).

*P.S. You can also install and use third-party network plugins with Docker.*

## Docker-Compose 

Used for running multi-container docker applications

Components:

- VERSION

- SERVICES (this will be the hostname of the services as well)

  - BUILD (location of the docker file that it will build)

    - CONTEXT

    - DOCKERFILE

    - ARGS

  - PORT (port of service)

  - VOLUMES

  - ENVIRONMENT

  - ENV_FILE

*Things to take note:*

1. docker-compose up -d (basic command)

2. docker-compose down (stops and removes)

3. running `docker-compose up` again will not rebuild if you did not delete the container (drawback of docker-compose)

4. docker-compose up -d --build (force rebuild)

5. Rebuilding your images will of course also not work if you are referencing an already built image

*Multi-Container Applications (still don't really know how this works)* 

1. Using Network Drivers

   1. See Docker-MERN project

   2. Network Bridge (DNS Resolution automatically done for you, just point the endpoints to localhost)

   3. Exception for database, database host will be mongo_docker

      1. This idea of database connection for docker containers and docker images can also be seen in Docker-Mongo-Node project, where the host is "mongo" as specified in the yaml file

2. Without Network Drivers 

  1. Then for the api servers, the frontend must point to the hostname of the api servers, which would be the name of the service within docker-compose)