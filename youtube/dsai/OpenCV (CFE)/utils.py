import cv2
import os
import glob
import numpy as np


# Setting of resolution


def make_1080p(cap):
    cap.set(3, 1920)
    cap.set(4, 1080)


def make_720p(cap):
    cap.set(3, 128000000)
    cap.set(4, 72000000)


def make_480p(cap):
    cap.set(3, 640)
    cap.set(4, 480)


def change_res(cap, width, height):
    cap.set(3, width)
    cap.set(4, height)


# Resizing of image width, height


def rescale_frame(frame, percent=75):
    width = int(frame.shape[1] * percent / 100)
    height = int(frame.shape[0] * percent / 100)
    dim = (width, height)
    return cv2.resize(frame, dim, interpolation=cv2.INTER_AREA)


def image_resize(image, width=None, height=None, inter=cv2.INTER_AREA):
    # initialize the dimensions of the image to be resized and
    # grab the image size
    dim = None
    (h, w) = image.shape[:2]
    # if both the width and height are None, then return the
    # original image
    if width is None and height is None:
        return image
    # check to see if the width is None
    if width is None:
        # calculate the ratio of the height and construct the
        # dimensions
        r = height / float(h)
        dim = (int(w * r), height)
    # otherwise, the height is None
    else:
        # calculate the ratio of the width and construct the
        # dimensions
        r = width / float(w)
        dim = (width, int(h * r))

    # resize the image
    resized = cv2.resize(image, dim, interpolation=inter)
    # return the resized image
    return resized


# Getting image width, height


def get_dims(video_to_capture, video_resolution='1080p'):
    width, height = STD_DIMENSIONS["480p"]
    if video_resolution in STD_DIMENSIONS:
        width, height = STD_DIMENSIONS[video_resolution]
    change_res(video_to_capture, width, height)
    return width, height


def get_width_height(width_division=1, height_division=1, screen=None):
    if screen is None:
        print("screen value not set and cannot be none!")
        return NotImplementedError
    screen_width = screen.winfo_screenwidth()
    screen_height = screen.winfo_screenheight()
    screen.destroy()
    WIDTH = screen_width / width_division
    HEIGHT = screen_height / height_division
    return WIDTH, HEIGHT


# Return video type


def get_video_type(video_path):
    # splitext splits file into root and extension
    video_path, ext = os.path.splitext(video_path)
    if ext in VIDEO_TYPE:
        return VIDEO_TYPE[ext]

    return VIDEO_TYPE['.avi']


# Encapsulate previous methods into a class


class VideoConfig(object):

    # Standard Video Dimensions Sizes
    STD_DIMENSIONS = {
        "360p": (480, 360),
        "480p": (640, 480),
        "720p": (1280, 720),
        "1080p": (1920, 1080),
        "4k": (3840, 2160),
    }

    # Video Encoding, might require additional installs
    # Types of Codes: http://www.fourcc.org/codecs.php
    VIDEO_TYPE = {
        'avi': cv2.VideoWriter_fourcc(*'XVID'),
        'mp4': cv2.VideoWriter_fourcc(*'XVID'),
    }

    width = 640
    height = 480
    dims = (640, 480)
    capture = None
    video_type = None

    def __init__(self, capture, filepath, res="480p", *args, **kwargs):
        self.capture = capture
        self.filepath = filepath
        self.width, self.height = self.get_dims(res=res)
        self.video_type = self.get_video_type()

    # Set resolution for the video capture
    # Function adapted from https://kirr.co/0l6qmh
    def change_res(self, width, height):
        self.capture.set(3, width)
        self.capture.set(4, height)

    def get_dims(self, res='480p'):
        width, height = self.STD_DIMENSIONS['480p']
        if res in self.STD_DIMENSIONS:
            width, height = self.STD_DIMENSIONS[res]
        self.change_res(width, height)
        self.dims = (width, height)
        return width, height

    def get_video_type(self):
        filename, ext = os.path.splitext(self.filepath)
        if ext in self.VIDEO_TYPE:
            return self.VIDEO_TYPE[ext]
        return self.VIDEO_TYPE['avi']


def images_to_videos(out, img_dir="images/timelapse", clear_images=True):

    # find stuff recursively in python
    # https://www.geeksforgeeks.org/how-to-use-glob-function-to-find-files-recursively-in-python/
    image_list = glob.glob(f'{img_dir}/*.jpg')
    sorted_images = sorted(image_list, key=os.path.getmtime)

    for file in sorted_images:
        image_frame = cv2.imread(file)
        out.write(image_frame)

    if clear_images:
        for file in image_list:
            os.remove(file)


# Apply filters


def apply_portrait_mode(frame):

    # have no idea what is portrait mode
    # apparently is some type of mode to add "depth effect" on videos
    # https://support.apple.com/en-sg/HT208118
    frame = verify_alpha_channel(frame)

    # convert gray pixels to white or block
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    _, mask = cv2.threshold(gray, 120, 255, cv2.THRESH_BINARY)
    mask = cv2.cvtColor(mask, cv2.COLOR_GRAY2BGRA)
    blur = cv2.GaussianBlur(frame, (21, 21), 11)
    blend = alpha_blend(frame, blur, mask)
    frame = cv2.cvtColor(blend, cv2.COLOR_BGRA2BGR)
    return frame


def apply_circle_focus_blur(frame):
    frame = verify_alpha_channel(frame)
    f_h, f_w, f_c = frame.shape
    y = int(f_h / 2)
    x = int(f_w / 2)
    radius = int(y/2)
    center = (x, y)
    mask = np.zeros((f_h, f_w, 4), dtype='uint8')

    # mask is the screen, center radius is the circle you know how it is
    # (255, 255, 255) is the white color within the circle
    # -1 is the thickness
    # cv2.LINE_AA is the line type
    cv2.circle(mask, center, radius, (255, 255, 255,), -1, cv2.LINE_AA)

    # applies blur on the whole screen
    # https://docs.opencv.org/4.5.2/d4/d13/tutorial_py_filtering.html
    mask = cv2.GaussianBlur(mask, (21, 21), 11)
    blur = cv2.GaussianBlur(frame, (21, 21), 11)

    blend = alpha_blend(frame, blur, 255-mask)
    frame = cv2.cvtColor(blend, cv2.COLOR_BGRA2BGR)
    return frame


def alpha_blend(frame_1, frame_2, mask):
    alpha = mask / 255.0
    blended = cv2.convertScaleAbs(frame_1 * (1 - alpha) + frame_2 * alpha)
    return blended


def apply_invert(frame):
    return cv2.bitwise_not(frame)


def apply_any_color(frame, b, g, r, intensity=0.5):
    frame = verify_alpha_channel(frame)
    frame_h, frame_w, frame_c = frame.shape
    color = (b, g, r, 1)
    overlay = np.full((frame_h, frame_w, 4), color, dtype='uint8')
    cv2.addWeighted(overlay, intensity, frame, 1.0, 0, frame)
    frame = cv2.cvtColor(frame, cv2.COLOR_BGRA2BGR)
    return frame


def apply_sephia(frame, intensity=0.5):
    # I tested it with just 3 channels and it works with just 3 channels so...
    frame = verify_alpha_channel(frame)
    frame_h, frame_w, frame_c = frame.shape
    sephia = (20, 66, 112, 1)
    overlay = np.full((frame_h, frame_w, 4), sephia, dtype='uint8')
    cv2.addWeighted(overlay, intensity, frame, 1.0, 0, frame)
    frame = cv2.cvtColor(frame, cv2.COLOR_BGRA2BGR)
    return frame


def verify_alpha_channel(frame):
    try:
        frame.shape[3]
    except IndexError:
        frame = cv2.cvtColor(frame, cv2.COLOR_BGR2BGRA)
    return frame


# Utils objects


VIDEO_TYPE = {
    '.avi': cv2.VideoWriter_fourcc(*'XVID'),
    '.mp4': cv2.VideoWriter_fourcc(*'H264'),
}

STD_DIMENSIONS = {
    "480p": (640, 480),
    "720p": (1280, 720),
    "1080p": (1920, 1080),
    "4k": (3840, 2160),
}
