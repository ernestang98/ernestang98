FROM node:14-slim

WORKDIR /usr/src/app

COPY ./package.json ./
COPY ./yarn.lock ./

RUN yarn

# ENV MONGO_URI=mongodb://localhost:27017/app
ENV MONGO_URI=mongodb://mongo_docker:27017/demo
# ENV MONGO_URI=mongodb+srv://admin:passwordisthepassword@testcluster.k1fi1.mongodb.net/instagram?retryWrites=true&w=majority

COPY . .

EXPOSE 5000

CMD [ "yarn", "start" ]