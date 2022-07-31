import glob
import math
import cv2
from utils import VideoConfig, apply_invert, apply_sephia, apply_circle_focus_blur, apply_portrait_mode

cap = cv2.VideoCapture(0)
path = 'timelapse.mp4'
fps = 3
config = VideoConfig(cap, path, '720p')
# out = cv2.VideoWriter(path, config.video_type, fps, config.dims)

while True:
    ret, frame = cap.read()
    frame = apply_portrait_mode(frame)
    # frame = apply_circle_focus_blur(frame)
    # frame = apply_invert(frame)

    # Alpha Channel automatically removed if you used BRGA
    # frame = cv2.cvtColor(frame, cv2.COLOR_BGR2BGRA)

    cv2.imshow('Frame', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
