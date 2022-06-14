import pickle
import cv2
from utils import rescale_frame

face_cascade = cv2.CascadeClassifier('data/haarcascade_frontalface_alt.xml')
recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read('data/face-recognizer.yml')

cap = cv2.VideoCapture(0)

labels = {"john-doe": 0}

with open("face-labels.pickle", 'rb') as f:
    og_labels = pickle.load(f)
    labels = {val: key for key, val in og_labels.items()}

while True:

    ret, frame = cap.read()
    frame = rescale_frame(frame, 80)
    grayFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(frame, scaleFactor=None, minNeighbors=5)

    for (x, y, w, h) in faces:
        color = (255, 0, 0)
        stroke = 2
        end_x = x + w
        end_y = y + h
        rec = cv2.rectangle(frame, (x, y), (x + w, y + h), color, stroke)
        bounding_box = frame[y:y+h, x:x+w]
        gray_bounding_box = grayFrame[y:y+h, x:x+w]
        id_, conf = recognizer.predict(gray_bounding_box)
        if 0 <= conf:
            print(id_, conf)
            print(labels[id_])
            cv2.putText(rec, labels[id_], (x, y - 10), cv2.FONT_HERSHEY_COMPLEX, 0.9, color)

        output_see_what_classifier_sees = "classifier.png"

        # cv2.imwrite(output_see_what_classifier_sees, bounding_box)

    # make sure imshow() is after .rectangle()
    cv2.imshow("Normal Frame", frame)

    if cv2.waitKey(1) & 0xFF == ord('a'):
        break

cap.release()
cv2.destroyAllWindows()
