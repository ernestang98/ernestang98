build-dev:
	docker build -t react-app-dev -f Dockerfile.dev .

###################

build-local:
	docker build \
		-t react-app-production:local \
		--build-arg CADDYFILE=Caddyfile.local \
		--build-arg BASE_URL=http://localhost:5000/api \
		-f Dockerfile.production .


	docker build \
		-t react-app-production:local \
		--build-arg CADDYFILE=Caddyfile.local \
		--build-arg BASE_URL=http://localhost:81/api \
		-f Dockerfile.production .

###################

build-production:
	docker build \
		-t react-app-production:production \
		--build-arg CADDYFILE=Caddyfile.production \
		--build-arg BASE_URL=https://mern.mysuperawesomesite.com/api \
		-f Dockerfile.production .




Basically, from the docker container, it only knows the existence of server by its hostname, you can try curling api-server:5000. But the reason why you will use localhost:3000 for the docker container even if it does not know that api server is on localhost:3000 is because YOUR browser can access localhost:3000 and can hence pull data from localhost:3000 which will be then displayed on localhost:5000

curl -I -X GET "http://localhost:80/api"

Reverse proxy redirects shit. You can try curling localhost:80/api IN THE DOCKER CONTAINER. If you set the proxy endpoint to google.com, then you will get some 404 error cause the request got forwarded to google.com/api. You can see the output via your browser by opening YOUR browser and entering localhost:81/api (you will see the same output as google.com/api)