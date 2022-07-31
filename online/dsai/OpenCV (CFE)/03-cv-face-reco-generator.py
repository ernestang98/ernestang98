import cv2
import os
import numpy as np
from PIL import Image
import pickle

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
image_dir = os.path.join(BASE_DIR, "images")

face_cascade = cv2.CascadeClassifier('data/haarcascade_frontalface_alt.xml')
recognizer = cv2.face.LBPHFaceRecognizer_create()

total_files = 0
rejected_files = 0
current_id = 1
label_ids = {}
y_labels = []
x_train = []
label = None
path = None
id_ = None

for root, dirs, files in os.walk(image_dir):

    # within each directory iterate all the files
    # tl/dr open each image of each folder of the images directory
    for file in files:

        # checks that it has an image extension
        if file.endswith("png") or file.endswith("jpg"):

            total_files += 1

            # path is the directory path of the object
            path = os.path.join(root, file)

            # label is the "category" of the person
            label = os.path.basename(root).replace(" ", "-").lower()

            # label_ids are a dictionary of labels, key is the label, value is the id number (current_id)
            if label not in label_ids:
                label_ids[label] = current_id
                current_id += 1

            # update id_ val to be the current_id value
            id_ = label_ids[label]

            # if label path or id_ is none then break
            if label is None or path is None or id_ is None:
                print("Exception")
                break

            # y_labels and x_train is an array of labels and paths respectively
            else:

                # process image
                # grayscale, must be if not error will be thrown
                pil_image = Image.open(path).convert("L")
                size = (850, 850)

                # What is ANTIALIAS
                # https://stackoverflow.com/questions/50192897/what-does-the-antialias-mean-in-this-font-render-code-for-pygame-mean
                # https://en.wikipedia.org/wiki/Spatial_anti-aliasing
                final_image = pil_image.resize(size, Image.ANTIALIAS)
                image_array = np.array(pil_image, "uint8")

                # detecting the face on the image
                faces = face_cascade.detectMultiScale(image_array, scaleFactor=None, minNeighbors=5)

                if faces == ():
                    print("cannot find ROI for path: " + path)
                    rejected_files += 1

                else:
                    for (x, y, w, h) in faces:
                        # ROI = Region Of Interest, AKA Bounding Box
                        roi = image_array[y:y + h, x:x + w]
                        # y_labels.append(label)
                        # x_train.append(path)
                        y_labels.append(id_)
                        x_train.append(roi)

with open("face-labels.pickle", 'wb') as f:
    pickle.dump(label_ids, f)

recognizer.train(x_train, np.array(y_labels))
recognizer.save("data/face-recognizer.yml")

# For every pic, its label and location is extracted
# x_train alternates between the label and its id (the face-detector may not have been able to properly identify the ROI
print("Total number of labels: " + str(len(y_labels)))
print("Total number of data: " + str(len(x_train)))
print("Total number of image files: " + str(total_files))
print("Total number of rejected files: " + str(rejected_files))
print("Total number of accepted files: " + str(total_files - rejected_files))
