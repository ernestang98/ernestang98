# Python 3.7.4
# OpenCV-Python 4.5.3.56

import cv2
import tkinter as tk
from utils import rescale_frame, make_1080p

cap = cv2.VideoCapture(0)
make_1080p(cap)

while True:
    ret, frame = cap.read()
    grayFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Upscale / Downscale method 1
    root = tk.Tk()
    screen_width = root.winfo_screenwidth()
    screen_height = root.winfo_screenheight()
    root.destroy()
    WIDTH = screen_width / 2
    HEIGHT = screen_height / 2
    frame = cv2.resize(frame, (int(WIDTH), int(HEIGHT)))

    # Upscale / Downscale method 2
    grayFrame = rescale_frame(grayFrame, 30)

    cv2.imshow("Normal Frame", frame)
    cv2.imshow('Grayscale Frame', grayFrame)

    # waitKey() basically determines how long cv waits for press key 'a', set to 1
    # ord('a') closes the browsers on 'a' keypress
    if cv2.waitKey(1) & 0xFF == ord('a'):
        break

cap.release()
cv2.destroyAllWindows()
