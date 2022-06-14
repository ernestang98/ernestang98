import cv2
import tkinter as tk
from utils import get_video_type, get_width_height

root = tk.Tk()
filename = "test.avi"
frames_per_seconds = 24.0
res = '720p'
WIDTH, HEIGHT = get_width_height(3, 3, root)

cap = cv2.VideoCapture(0)

# 1st param is filename, 2nd param is video codec (FourCC is a 4 byte code used to specify the video codec,
# video format), 3rd param is the frame per seconds, 4th param is the height and width
out = cv2.VideoWriter(filename, get_video_type(filename), 25, (int(WIDTH), int(HEIGHT)))

while True:
    ret, frame = cap.read()
    frame = cv2.resize(frame, (int(WIDTH), int(HEIGHT)))
    grayFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    out.write(frame)
    cv2.imshow("Normal Frame", grayFrame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
out.release()
cv2.destroyAllWindows()
