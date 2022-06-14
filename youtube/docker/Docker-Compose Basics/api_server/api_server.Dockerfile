FROM node:12.18-slim

RUN apt-get update -y
RUN apt-get install -y nano
RUN apt-get install -y sudo

RUN usermod -aG sudo -u 1001 node
RUN passwd -d node
USER node

WORKDIR /home/node/src
# CMD ["/bin/bash"]

# ARG API_PORT
# ENV PORT=$API_PORT
# EXPOSE $API_PORT

ENV PORT=12345
EXPOSE 12345
