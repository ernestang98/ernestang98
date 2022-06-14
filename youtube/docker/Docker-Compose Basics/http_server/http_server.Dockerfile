FROM ubuntu:18.04

RUN apt-get update -y
RUN apt-get install -y nano
RUN apt-get install -y python3.8
RUN apt-get install -y sudo

RUN useradd http_server_user -p 'temporary' -u 1001 -m
RUN passwd -d http_server_user
RUN usermod -aG sudo http_server_user

USER http_server_user
WORKDIR home/http_server_user/src

# ARG HTTP_PORT
# ENV PORT=$HTTP_PORT
# EXPOSE $HTTP_PORT

ENV PORT=8888
EXPOSE 8888
