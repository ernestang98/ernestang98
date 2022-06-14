import cv2
import imagezmq
import socket
import ImageReceive
import sys

image_hub = imagezmq.ImageHub()
print("Image server started")

while True:
    rpi_name, image = image_hub.recv_image()
    print("laptop is receiving images")
    #need rpi to send msg that is done!
    if rpi_name == "STOP_SERVER":
        sys.exit(0)

    # If this does not work, change from .png to .jpg
    output_dir = r'C:\Users\gohti\OneDrive\Desktop\yolov5-master\outputFromRpi.jpg' # TODO: Change this. Retain 'r' at front
    cv2.imwrite(output_dir, image)
    print("Receiving image, sending to image processing...")
    message = ImageReceive.main()
    if message is None:
        message = "IMG|RPI|NO"
    message = message.encode('utf-8')
    image_hub.send_reply(message)

