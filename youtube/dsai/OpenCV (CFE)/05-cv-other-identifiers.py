import cv2
from utils import rescale_frame

# cp -R /path/to/venv/lib/python3.7/site-packages/cv2/data /path/to/this/directory
face_cascade = cv2.CascadeClassifier('data/haarcascade_frontalface_alt2.xml')
eye_cascade = cv2.CascadeClassifier('data/haarcascade_eye.xml')

# this one is pretty bad, maybe don't use LOL
smile_cascade = cv2.CascadeClassifier('data/haarcascade_smile.xml')

cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    frame = rescale_frame(frame, 50)

    # scaleFactor creates scale pyramid
    # https://stackoverflow.com/questions/20801015/recommended-values-for-opencv-detectmultiscale-parameters
    # 1.1 means downscale image by 10%, making it harder to detect a face I suppose
    faces = face_cascade.detectMultiScale(frame, scaleFactor=None, minNeighbors=5)

    # these coordinates are in pixels (499, 232, 288, 288)
    for (x, y, w, h) in faces:
        color = (255, 0, 0)
        stroke = 2
        end_x = x + w
        end_y = y + h
        rec = cv2.rectangle(frame, (x, y), (x + w, y + h), color, stroke)
        cv2.putText(rec, "Face", (x, y-10), cv2.FONT_HERSHEY_COMPLEX, 0.9, color)

        reco = eye_cascade.detectMultiScale(frame, scaleFactor=None, minNeighbors=5)

        for (ex, ey, ew, eh) in reco:
            cv2.rectangle(frame, (ex, ey), (ex + ew, ey + eh), (0, 0, 255), stroke)

        # frame[y:y+h, x:x+w] is actually [coord-y-start:coord-y-end, coord-x-start:coord-x-end]
        bounding_box = frame[y:y+h, x:x+w]
        output_see_what_classifier_sees = "classifier.png"

        cv2.imwrite(output_see_what_classifier_sees, bounding_box)

    # make sure imshow() is after .rectangle()
    cv2.imshow("Normal Frame", frame)

    if cv2.waitKey(1) & 0xFF == ord('a'):
        break

cap.release()
cv2.destroyAllWindows()
