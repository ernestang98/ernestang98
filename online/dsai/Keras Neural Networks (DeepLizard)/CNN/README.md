### [Keras Convolutional Neural Network (CNN) - till 1:57:50](https://www.youtube.com/watch?v=qFJeN9V1ZsI)

### CNN use cases:

Mainly for images

Training/Testing Data used:

- https://www.kaggle.com/c/dogs-vs-cats/data?select=train.zip
- https://www.microsoft.com/en-us/download/details.aspx?id=54765
- [**Google Drive link: download cats-and-sogs.zip**](https://drive.google.com/drive/folders/1kkR1TBaPXdTVI5Y6jbdRn2bbKLpDLIaW?usp=sharing)

Steps per epoch:

- how many times you will fetch from the generator during each epoch

### Resources:

- https://stackoverflow.com/questions/44747343/keras-input-explanation-input-shape-units-batch-size-dim-etc

- https://towardsdatascience.com/a-comprehensive-guide-to-convolutional-neural-networks-the-eli5-way-3bd2b1164a53

- https://www.youtube.com/watch?v=FmpDIaiMIeA

- https://developers.google.com/machine-learning/data-prep/transform/normalization

- https://medium.com/ml-cheat-sheet/understanding-non-linear-activation-functions-in-neural-networks-152f5e101eeb#:~:text=What%20does%20non%2Dlinearity%20mean,boundary%20which%20is%20not%20linear.

- https://www.superdatascience.com/blogs/convolutional-neural-networks-cnn-step-1b-relu-layer/

- https://machinelearningmastery.com/pooling-layers-for-convolutional-neural-networks/

- https://stackoverflow.com/questions/51885739/how-to-properly-set-steps-per-epoch-and-validation-steps-in-keras

### Theory:

ANN structure: input -> hidden -> hidden ... -> hidden -> output

CNN structure: convolutional (filter) -> RELU (normalization) -> pooling (compression & reduction) -> convolutional -> RELU -> pooling -> ... -> flattern -> fully connected -> softmax (classifier which uses cross entropy) -> output

1. Filters: used to identify features, produce feature map

2. RELU: used to increase non-linearity

3. Pooling: down samples and decreases the specificity and resolution of the features in the feature map

### Parameters (Conv2D):

1. filters: number of filters used

2. kernel_size: size of filter

3. pool_size: size of pooling window

4. strides: number of pixel it will move at a time for the movement of the windows

5. padding: valid or same https://stackoverflow.com/questions/52166594/how-to-use-padding-in-conv2d-layer-of-specific-size

### Other things:

| VGG16                                                        | MobileNet                                          |
| ------------------------------------------------------------ | -------------------------------------------------- |
| Pre-trained Model <br />Won the 2014 ImageNet competition <br />ImageNet: database/library of thousands of images <br />CNN for classificarion and detection | Smaller and lighter in size <br />Light weight CNN |

- Both VGG16 and MobileNet are functional model types

- For VGG16 we converted it to sequential model type (Sequential())

- for MobileNet, we will stick to functional model type (Model())

- For MobileNet, play around with until which layer you would want to freeze trainable-ility

- https://www.machinecurve.com/index.php/question/what-are-trainable-and-non-trainable-parameters-in-model-summary/

- Set layers as untrainable if you don't want to re-train the model

- DATA AUGMENTATION in keras - If image dataset not big enough, can use ImageDataGenerator(), prevent overfitting

- FINE TUNING: Set the last layer and change the number of units to the number of classifications you require (if binary classification, change default 1000 to 2)
