## Typical Dockerfile components:

1. FROM **IMAGE:TAG**  as **IMAGE_REFERENCE**

   - Underlying image of Dockerfile (Node, Gradle, Nginx etc.)

   - Underlying software which your code will sit upon

2. ENV

   - Environment variables accessible by application (process.env.VAR for node.js)

3. ARG

   - Argument variables

   - You can specify default values if not specified when docker build is run

4. WORKDIR **/${app}**

   - Working directory of image, where all your code will subsequently be places

   - default is root directory (e.g. /)

   - if you specify a directory (e.g. /app), then all the source code will subsequently be copied there

5. COPY 

   - Copy stuff over from the directory of the docker file to the location within the docker image

   - e.g. (COPY lol.txt . will copy lol.txt into the working directory specified above of the docker image)

6. RUN

   - Run stuff

7. EXPOSE **PORT_NUMBER**

   - Open a port (e.g. EXPOSE 3000 opens port 3000)

8. CMD ["XXX", "XXX"]

   - Set default command which will be executed opnly when you run container without specifying a command

   - Can set parameters

   - Can be overwritten (see article)

9. ENTRYPOINT ["XXX", "XXX"]

   - Set a command which container will run as an executable

   - Allows you to set parameters as well

   - Command and parameters will NOT be overwritten

## Docker commands with dockerfile

docker build -t DOCKER_CONTAINER . (finds dockerfile in the current directory, specified by **"."**)

docker build -t DOCKER_CONTAINER -f Dockerfile.special . (finds Dockerfile.special)

docker build --env-file=/path/to/envfile -t DOCKER_CONTAINER . (build docker with envfile)

docker build --env-file=/path/to/envfile --build-arg PORT=3000 --build-arg SECRET="not so secret" -t DOCKER_CONTAINER . (build docker with arguments)

## Additional Articles:

CMD vs ENTRYPOINT vs RUN: https://goinbigdata.com/docker-run-vs-cmd-vs-entrypoint/

ENV and ARG in Dockerfile: https://blog.bitsrc.io/how-to-pass-environment-info-during-docker-builds-1f7c5566dd0e

Multiple --build-args: https://stackoverflow.com/questions/42297387/docker-build-with-build-arg-with-multiple-arguments