### [Keras Artificial Neural Network (ANN) - till 1:01:25](https://www.youtube.com/watch?v=qFJeN9V1ZsI)

Mainly for categorical and continuous variables

### What is Keras?

High level Neural Network API fully integrated with Tensorflow library

Used to sit on top of one of the 3 lower level API backend engines:

1. Tensorflow

2. Theano

3. CNTK

### Foundation:

1. Tensor -> multidimensional array with a uniform type called dtype (similar to Numpy arrays: np.array())

2. https://www.tensorflow.org/guide/tensor

3. Training Labels: what we are predicting

4. Training samples: what we will use to train our neural network

5. cost function -> loss function, [basically the margin of error](https://radiopaedia.org/articles/cost-function-machine-learning)

6. training set: train the dataset

7. validation: hyperparameter truning

8. test set: test the dataset

9. [validation split](https://datascience.stackexchange.com/questions/38955/how-does-the-validation-split-parameter-of-keras-fit-function-work): ratio of training data used for validation during model training

10. val_loss is the loss from validation set

11. loss is the cost from train set

12. [loss vs val_loss](https://www.kaggle.com/c/tgs-salt-identification-challenge/discussion/62261)

13. [Epoch vs Batch Size vs Iteration](https://towardsdatascience.com/epoch-vs-iterations-vs-batch-size-4dfb9c7ce9c9)

### Sequential Model Object:

Link: https://www.tensorflow.org/api_docs/python/tf/keras/Sequential

When to use:

1. Plain Stack of layers

2. One input tensor

3. One output tensor

4. What it looks like:
   ```bash
   model = Sequential([
       Dense(units=16, input_shape=(1,), activation='relu')
   ])
   ```

   - Units refer to the nodes
   
   - Activation refers to the activation function used
   
   - input_shape refers to the shape of the inputs, in this case, (1,) means 1 column training data
   
     - [E.g. if you have 75 columns and 14000 rows, then your input_shape would be (75,)](https://datascience.stackexchange.com/questions/53609/how-to-determine-input-shape-in-keras)
     
   - Dense refers to hidden layer, more Denses = more hidden layers
   
      - first layer is usually the input layer, various ways of indicating this as seen in [docs](https://keras.io/guides/sequential_model/#specifying-the-input-shape-in-advance)
      
      - last layer is the output layer, for categorical variables, number of nodes = to number of categories
      
   - activation: activation function, determines how activated a neuron will be, also used to introduce non-linearity to the neural network
   
   - Optimizers: reduces loss
   
   - loss: type of loss metric to be reduced as much as possible during training
   
   - metrics: type of metrics used to determine performance of model -> not used during training
   
   - batch_size: number of sample to be used in each propagation of your neural network
   
   - epochs: number of passes -> one pass refers to one cycle of using all of the training data to train the neural network. Consist of forward pass and backward pass
   
   - verbose: visualization purposes, either 0, 1 or 2
   
   - model.fit() to train the model
   
   - model.predict() to perform predictions

### Data cleaning processing:

1. Transform samples and labels to fit the input and output of Sequential Model Object

2. Normalize the sample data

3. Transform the training sample if needed (for sequential model, it cannot work with 1d data)

4. Train the model, hyperparameter tuning

5. Test, validate the model you know how it is

