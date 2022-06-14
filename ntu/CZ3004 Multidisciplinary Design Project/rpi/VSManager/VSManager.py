import sys
import socket
import time
import cv2
from imutils.video import VideoStream
import imagezmq
from UtilsManager import UtilsManager
import multiprocessing
import threading


class VSManager(multiprocessing.Process):
    def __init__(self, process_queue, id):
        multiprocessing.Process.__init__(self)
        self.process_queue = process_queue
        self.id = id
        self.vs = None
        self.sender = None
        self.rpi_name = None
        self.tcp_connection = True
        self.handle_queue = multiprocessing.Manager().Queue()
        self.timeout = 5
        self.sleep_counter = 0
        self._start = False
        self.start()


    def run(self):
        t1 = threading.Thread(target=self.handle_and_send_packet, args=())
        t3 = threading.Thread(target=self.video_stream, args=())
        t1.start()
        t3.start()


    def video_stream(self):
        UtilsManager.log("LOG", self.id, "Setting up video stream...")
        while True:
            if self._start:

                UtilsManager.log("LOG", self.id, "Starting video stream...")

                if self.vs == None:
                    UtilsManager.log("ERR", self.id, "Error... there is no video stream... creating one")
                    # self.vs = VideoStream(usePiCamera=True, resolution =(368,480))
                    self.vs = VideoStream(usePiCamera=True, resolution =(920,1080))

                if self.tcp_connection:
                    self.sender = imagezmq.ImageSender(connect_to='tcp://192.168.34.12:5555')
                    self.rpi_name = socket.gethostname()

                    self.vs.start()
                    time.sleep(2.0)

                while self._start:
                    if self.tcp_connection:

                        image = self.vs.read()
                        message = self.sender.send_image_reqrep(self.rpi_name, image)

                        message = message.decode("utf-8")
                        UtilsManager.log("LOG", self.id, "Received following from server: {}".format(message))
                        if 'NO' in message:
                            UtilsManager.log("LOG", self.id, "Sending 41 to Android")
                            self.process_queue.put("IMG|AND|41")
                            self._start = False

                        elif 'NO' not in message: # A valid reply from image server
                            self.process_queue.put(message)
                            UtilsManager.log("LOG", self.id, "Valid reply: {} from IMG server".format(message))
                            self._start = False

                        #elif self.sleep_counter >= self.timeout:
                        #    self.sleep_counter = 0

                            #UtilsManager.log("LOG", self.id, "Sending 41 to Android")
                            #self.process_queue.put("IMG|AND|41")
                        # else:
                        #     print("should move 10 forward and backwards")

                    else:
                        UtilsManager.log("LOG", self.id, "self.tcp_connection disabled")
                        time.sleep(2.0)

                if not self._start:
                    UtilsManager.log("LOG", self.id, "Stopping video stream...")
                    self._start = False


    def send_message_to_server(self, packet):
        if self.sender == None:
            UtilsManager.log("ERR", self.id, "TCP server and client disconnected!")
        else:
            self.sender.send_image(packet, None)


    """
    We only send "STOP_SERVER" to TCP Server for now
    """
    def handle_and_send_packet(self):
        while True:
            if(self.handle_queue.qsize()!=0):
                packet = self.handle_queue.get()
                self.handle_queue.task_done()
                if packet == "START_PI_CAM":
                    self.sleep_counter=0
                    self._start = True
                elif packet == "STOP_PI_CAM":
                    # time.sleep(self.timeout-self.sleep_counter+1)
                    self._start = False
                elif packet == "STOP_SERVER":
                    self.send_message_to_server(packet)
                elif packet == "V":
                    self.sleep_counter=0
                    self._start = True
                else:
                    UtilsManager.log("LOG", self.id, "Well... we aren't suppose to send anything else to picam... hence throwing {}".format(packet))
            time.sleep(0.000001)


    def handle_packet(self, packet):
        self.handle_queue.put(packet)
