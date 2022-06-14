import os
import datetime
import cv2
import time

from utils import VideoConfig, images_to_videos

cap = cv2.VideoCapture(0)
path = 'timelapse.mp4'
fps = 3
config = VideoConfig(cap, path, '720p')
out = cv2.VideoWriter(path, config.video_type, fps, config.dims)

if not os.path.exists("images/timelapse/"):
    os.mkdir("images/timelapse/")

now = datetime.datetime.now()
finish_time = now + datetime.timedelta(seconds=10)
i = 0

while datetime.datetime.now() < finish_time:

    ret, frame = cap.read()
    # out.write(frame)
    # cv2.imshow('Frame', frame)

    filename = f"images/timelapse/{i}.jpg"
    i += 1
    time.sleep(1)
    cv2.imwrite(filename, frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break


images_to_videos(out)

cap.release()
out.release()
cv2.destroyAllWindows()
