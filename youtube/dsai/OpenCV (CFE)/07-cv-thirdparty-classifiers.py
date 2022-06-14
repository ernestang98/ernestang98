import cv2
from utils import VideoConfig, image_resize

'''
CFE Tutorial on OpenCV:
OpenCV & Python Tutorial Video Series: https://kirr.co/ijcr59

List/Compilation of third-party OpenCV Haar Cascade classifiers (not working)
Eyes Cascade (and others): https://kirr.co/694cu1

Tutorial reference on adding mustache to nose
Nose Cascade / Mustache Post: https://kirr.co/69c1le
'''

# Similar to 06, instead of finding the bottom left hand of the screen, find where the eyes are, then replace those
# pixels with sunglasses on every frame via setting an overlay

cap = cv2.VideoCapture(0)

save_path = 'glasses_and_stash.mp4'
frames_per_seconds = 24
config = VideoConfig(cap, filepath=save_path, res='720p')
out = cv2.VideoWriter(save_path, config.video_type, frames_per_seconds, config.dims)
face_cascade = cv2.CascadeClassifier('data/haarcascade_frontalface_default.xml')
eyes_cascade = cv2.CascadeClassifier('data/thirdparty-frontaleyes.xml')
nose_cascade = cv2.CascadeClassifier('data/thirdparty-nose.xml')
glasses = cv2.imread("others/fun/glasses.png", -1)
mustache = cv2.imread('others/fun/mustache.png', -1)

while True:

    # Capture frame-by-frame
    ret, frame = cap.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray, scaleFactor=None, minNeighbors=5)

    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2BGRA)

    for (x, y, w, h) in faces:
        roi_gray = gray[y:y + h, x:x + h]
        roi_color = frame[y:y + h, x:x + h]
        # cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 255, 255), 3)

        eyes = eyes_cascade.detectMultiScale(roi_gray, scaleFactor=None, minNeighbors=7)
        for (ex, ey, ew, eh) in eyes:
            # cv2.rectangle(roi_color, (ex, ey), (ex + ew, ey + eh), (0, 255, 0), 3)
            roi_eyes = roi_gray[ey: ey + eh, ex: ex + ew]
            glasses2 = image_resize(glasses.copy(), width=ew)

            gw, gh, gc = glasses2.shape
            for i in range(0, gw):
                for j in range(0, gh):
                    # print(glasses[i, j]) #RGBA
                    if glasses2[i, j][3] != 0:  # alpha 0
                        roi_color[ey + i, ex + j] = glasses2[i, j]

        nose = nose_cascade.detectMultiScale(roi_gray, scaleFactor=None, minNeighbors=15)
        for (nx, ny, nw, nh) in nose:
            # cv2.rectangle(roi_color, (nx, ny), (nx + nw, ny + nh), (255, 0, 0), 3)
            roi_nose = roi_gray[ny: ny + nh, nx: nx + nw]
            mustache2 = image_resize(mustache.copy(), width=nw)

            mw, mh, mc = mustache2.shape
            for i in range(0, mw):
                for j in range(0, mh):
                    # print(glasses[i, j]) #RGBA
                    if mustache2[i, j][3] != 0:  # alpha 0
                        roi_color[ny + int(nh / 2.0) + i, nx + j] = mustache2[i, j]

    # Display the resulting frame
    frame = cv2.cvtColor(frame, cv2.COLOR_BGRA2BGR)
    out.write(frame)
    cv2.imshow('frame', frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# When everything done, release the capture
cap.release()
out.release()
cv2.destroyAllWindows()
