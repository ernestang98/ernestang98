import socket
import time

import threading
import multiprocessing

import queue as Queue

from UtilsManager import UtilsManager

class SocketManager(multiprocessing.Process):
    print_lock = threading.Lock()

    def __init__(self, process_queue, host, port, id):
        multiprocessing.Process.__init__(self)
        self.host = host
        self.port = port
        self.process_queue = process_queue
        self.handle_queue = multiprocessing.Manager().Queue()
        self.id = id
        self.conn = None
        self.daemon = True
        self.start()

    def run(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((self.host, self.port))
        s.listen()
        UtilsManager.log("LOG", self.id,"listening for connection")
        # to allow PC to reconnect at any given time
        while True:
            self.conn, address = s.accept()
            UtilsManager.log("LOG", self.id, "Connection from: " + str(address[0]) +":"+ str(address[1]))
            t1 = threading.Thread(target=self.socket_receive_packet, args=())
            t2 = threading.Thread(target=self.handle_and_send_packet)
            t1.start()
            t2.start()
            # t1.join()
            # t2.join()

    def socket_receive_packet(self):
        while True:
            try:
                data = self.conn.recv(1024)
                data = data.decode('utf-8').strip()
                data = data.replace("\r", "")
                packet = data.split(":")
                if not data:
                    break
                if len(data) > 0:
                    data = "".join(packet[0].split()).strip()
                    UtilsManager.log("LOG", self.id, "Data Received: " + data)
                    self.process_queue.put(packet[0])
            except socket.error as e:
                print(socket.error)
                self.logger.debug(e)
                self.print_lock.release()
                break
            time.sleep(0.0001)
        self.conn.close()


    def socket_send_packet(self, message):
        UtilsManager.log("LOG", self.id, "Sending " + message + " to Algo Java UI")
        self.conn.send(message.encode())

    """
    Called by t1 and when PacketHandler() calls handle_packet, sends packet to Android 
    via client socket
    """
    def handle_and_send_packet(self):
        while True:
            if(self.handle_queue.qsize()!=0):
                packet = self.handle_queue.get().strip()
                self.handle_queue.task_done()
                self.socket_send_packet(packet)
            time.sleep(0.000001)

    """
    Function used by PacketHandler to put packets meant for ## on the handle_queue.
    handle_and_send_packet() should be invoked upon handle_packet().
    """
    def handle_packet(self, packet):
        self.handle_queue.put(packet)
