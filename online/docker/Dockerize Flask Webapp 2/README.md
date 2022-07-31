## Coursera Guided Projects

1. [Docker Fundamentals](https://www.coursera.org/projects/docker-fundamentals)

2. [Create Docker Container Seaborn Regression Plot App](https://www.coursera.org/projects/create-docker-container-seaborn-regression-plot-app)

## Docker Flask Seaborn Example

Simple example of a dockerized flask app

## Quick Start

```bash
docker build --tag test .
docker run -it --name test -p5000:5000 test:latest
```