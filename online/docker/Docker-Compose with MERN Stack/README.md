## [Deploying a MERN Application (with Docker, Atlas, and Digital Ocean!)](https://www.youtube.com/watch?v=DftsReyhz2Q)

## My Edits:

- OPTIONAL: use  `make`  and  `makefile`

  - make and makefiles are just ways of running scripts automatically

  - Can refer to them to know how to build images etc.

- SERVER things

  - npm start looks at .env file in root directory 

  - if you build your docker image, there is no env variable (db config endpoint) within dockerfile

  - only when you build your container using docker-compose, will you specify you env variables under config

    - Local-env: localhost

    - Dev-env: mongo_docker

    - Prod-env: cloud

- CLIENT things

  - npm start looks at .env file in root directory 

  - Dockerfile.dev (development)

    - Used for docker-dev

    - Specifies environment variables (api endpoints) within dockerfile

  - Dockerfile.production (deployment)

    - 2 Stages:

      - npm run build

      - caddy (something like nginx but caddy has alot more good stuff - auto https, reverse-proxy, file-server)

    - Specify environment variables (api endpoints) using arguments (--build-args)

      - 2 Setups because of Caddy:

        - Local

        - Production

          - For Production to work, you need to make sure the mongo_docker image is running, hence just build docker production local first
      
- Dockerize your server first

  - ignore env variables you will be using locally

  - when you docker build, add ENV into your dockerfile

  - Mongo host: mongo_docker

- Dockerize your client

  - when it comes to environment variables, same concept
  
  - ignore env variables
  
  - add ENV into your dockerfile
  
  - Base url for api -> http://<react_app>:5000/api
    
- Attempt to docker-compose your client and server images in development mode

- Attempt to docker-compose you client and server images in production local mode

  - Note that you don't have to rebuild the image for servers because you can control the mongo uri end point via env files of DOCKER-COMPOSE

  - You will need to build a separate image for your client in production mode

- Attempt to docker-compose your client and server images in production deploy mode (on the cloud)

  *these are brief instructions, go to tutorial for more details*

  - Go to digital oceans, set up basic plan

  - create droplet (ubuntu)

  - ensure SSH, HTTP and HTTPS is set up

  - Go to Cloudflare, create domain name, under DNS management, point your domain name to the IP of your ubuntu droplet, ensure ssl tls encryption mode is flexible (optional to use ssl/tls)

    - This is because you have not generated your certificate

  - scp (secure copy) over all of your files into your server

  - docker-compose build your production images on your ubuntu server

  - upgrade tls ssl encryption to full

    - Will not work if you don't do this step because your config file in production enforces it to be on port 443
