## [Docker Tensorflow Serving](https://www.coursera.org/learn/tensorflow-serving-docker-model-deployment/reviews)

Most machine models aren't deployed due to the fact that many people believe that they are too hard to do so

Using docker can be a great solution to deploy a tensorflow server



Traditional solution:

- Set up a flask/django web server
- Send requests to python server
- Python server loads model with the input
- Python server creates a JSON response to the initial request



Problems:

- See screenshot
- Hence... TENSORFLOW SERVING



#### Tensorflow Serving:

- Production ready
- Part of TFX Ecosystem
- Used internally by Google
- Highly scalable model serving solution
- Works well for large models up to 2 GB



#### Tensorflow Serving Demo:

- `docker pull tensorflow/serving`
- `docker run -p 8500:8500 -p 8501:8501 -mount type=bind source=/path/to/model/, target=/models/my_model -e MODEL_NAME=my_model -t tensorflow/serving`

- `python3 tf_serving_grpc_client.py`
- `python3 tf_serving_rest_client.py`



#### Tensorflow Notes:

- Note that when you generate new tensorflow models, tensorflow serving automatically unloads old versions of the trained and saved model while also automatically uploading the newest versions of the trained and saved model



### Data Files:

[Google Drive link - download Reviews.csv, test.csv, and train.csv](https://drive.google.com/drive/folders/1kkR1TBaPXdTVI5Y6jbdRn2bbKLpDLIaW?usp=sharing)
