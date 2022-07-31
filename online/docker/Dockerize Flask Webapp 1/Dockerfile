FROM python:3.8

WORKDIR /usr/src/app

RUN pip3 install flask

COPY app.py ./app.py

ENTRYPOINT FLASK_APP=./app.py flask run --host=0.0.0.0