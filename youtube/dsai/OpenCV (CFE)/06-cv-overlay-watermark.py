import cv2
from utils import VideoConfig, image_resize
import numpy as np

cap = cv2.VideoCapture(0)

path = "watermark.avi"

# https://answers.opencv.org/question/16522/video-recording-is-too-fast/
# determines how fast the recorded video would be
fps = 10
resolution = '720p'
config = VideoConfig(cap, filepath=path, res=resolution)
out = cv2.VideoWriter(path, config.get_video_type(), fps, config.get_dims(resolution))

# Do it before the loop, since you don't need to resize it on every loop
logo_path = 'others/logo/beep.png'
watermark = cv2.imread(logo_path, -1)
watermark = image_resize(watermark, 100, 100)
print(watermark.shape)
# cv2.imshow('watermark', watermark)

# create 4 channel for overlay
watermark = cv2.cvtColor(watermark, cv2.COLOR_BGR2GRAY)
watermark = cv2.cvtColor(watermark, cv2.COLOR_GRAY2BGR)
watermark = cv2.cvtColor(watermark, cv2.COLOR_BGR2BGRA)
print(watermark.shape)

while True:
    ret, frame = cap.read()
    frame = cv2.resize(frame, config.get_dims(resolution))
    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2BGRA)

    # frame.shape has basically 3 values: (height, width, color - RGB hence got 3)
    frame_h, frame_w, frame_c = frame.shape

    # see frame.shape for gray-scaled images
    # grayscale = cv2.cvtColor(frame.copy(), cv2.COLOR_BGR2GRAY)
    # print(grayscale.shape)

    overlay = np.zeros((frame_h, frame_w, 4), dtype='uint8')
    # overlay[100:250, 100:125] = (255, 255, 0, 1)
    # overlay[100:250, 150:255] = (255, 0, 0, 1)
    # cv2.imshow("Overlay", overlay)
    # cv2.addWeighted(overlay, 0.25, frame, 1.0, 0, frame)

    # x = 50
    # y = 150
    # color = (255, 0, 0)
    # stroke = 2
    # w = 100
    # h = 200
    # cv2.rectangle(frame, (x, y), (x + w, y + h), color, stroke)

    watermark_h, watermark_w, watermark_c = watermark.shape
    for i in range(0, watermark_h):
        for j in range(0, watermark_w):
            # this is for png, png has its own 4th/alpha channel
            # jpeg does not have 4th alpha channel (always at 1)
            # print(watermark[i, j])
            if watermark[i, j][3] != 0:
                move_by_x = frame_h - watermark_h
                move_by_y = frame_w - watermark_w
                overlay[move_by_x + i, move_by_y + j] = watermark[i, j]

    cv2.addWeighted(overlay, 0.7, frame, 1.0, 0, frame)

    # final frame output must be BGR when saving video if not it will not work!
    frame = cv2.cvtColor(frame, cv2.COLOR_BGRA2BGR)

    # 1 pixel is defined by RGB
    # R/G/B can take a value from 0 - 255 (8 bit, 1 byte number)
    # Each frame is an array of an array of an array of 24 bits (3 bytes, one for R one B one G)
    # 1 array 1 pixel
    # An array of that array of pixels is probably the X-axis or Y-axis
    # An array of that array of that array of pixels is the entire frame
    # print(frame[x:x+w, y:y+h], end="\n========\n========\n=======\n")
    out.write(frame)
    cv2.imshow("Normal Frame", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
out.release()
cv2.destroyAllWindows()
