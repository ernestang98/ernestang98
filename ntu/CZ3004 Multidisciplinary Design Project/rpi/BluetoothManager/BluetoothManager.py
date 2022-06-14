import multiprocessing
from UtilsManager import UtilsManager
from bluetooth import *
import threading
import time

class BluetoothManager(multiprocessing.Process):

    def __init__(self, process_queue, id, debug = True):
        multiprocessing.Process.__init__(self)
        self.debug = debug
        self.process_queue = process_queue
        self.id = id
        self.port = None
        self.server_sock = BluetoothSocket(RFCOMM)
        self.daemon = True
        self.client_sock = None
        self.client_info = None
        self.handle_queue = multiprocessing.Manager().Queue()
        self.start()

    """
    Function to run on start()
    1. Starts t1 thread to handle messages meant for android via handle_and_send_packet()
    2. Opens Bluetooth socket & advertises service (meant for android to connect)
    3. On connection, if debug = True (True by default), sends "Android, can you receive me?" to Android
    4. Starts t2 thread to reeive messages via receive_message

    NOTES:
    whitelisting not implemented
    i think t1 not needed for now...
    """
    def run(self):
        t1 = threading.Thread(target=self.handle_and_send_packet, args=())
        t1.start()
        self.server_sock.bind(("", PORT_ANY))
        self.server_sock.listen(1)
        self.port = self.server_sock.getsockname()[1]
        uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"
        advertise_service(
            self.server_sock,
            "MDP-Server",
            service_id = uuid,
            service_classes = [ uuid, SERIAL_PORT_CLASS ],
            profiles = [ SERIAL_PORT_PROFILE ],
        )
        UtilsManager.log("LOG", self.id, "waiting for connection on RFCOMM channel %d" % self.port)
        self.client_sock, self.client_info = self.server_sock.accept()
        UtilsManager.log("LOG", self.id, "Accepted connection from {}".format(self.client_info))
        try:
            while True:
                if self.debug:
                    self.send_message("Android, can you receive me?")
                t2 = threading.Thread(target=self.receive_message, args=())
                t2.start()
                t2.join()
        except IOError as e:
            UtilsManager.log("ERR", self.id, str(e))

        UtilsManager.log("INFO", self.id, "disconnecting...")
        self.client_sock.close()
        self.server_sock.close()
        UtilsManager.log("INFO", self.id, "server and client sockets disconnected!")


    """
    Called by t2 after bluetooth socket connected and stuff. Receive message from android,
    Puts message send by android on the process_queue to be processed and redirected by PacketHandler
    """
    def receive_message(self):
        while True:
            try:
                data = self.client_sock.recv(1024)
                if len(data)>0:
                    packet = data.decode('utf-8').strip()
                    UtilsManager.log("LOG", self.id, "Received Data From Android: " + packet)
                    self.process_queue.put(packet)

            except BluetoothError as e:
                UtilsManager.log("ERR", self.id, str(e))
                self.reconnect()

            time.sleep(0.00001)

        self.client_sock.close()
        self.client_sock = None


    """
    Function used by PacketHandler to put packets meant for android on the handle_queue.
    handle_and_send_packet() should be invoked upon handle_packet().
    """
    def send_message(self, message): 
        if self.client_sock == None:
            UtilsManager.log("ERR", self.id, "server and client sockets disconnected!")
        else:
            # LOGIC HERE
            self.client_sock.send(str(message))
            time.sleep(0.6)


    """
    Called by t1 and when PacketHandler() calls handle_packet, sends packet to Android 
    via client socket
    """
    def handle_and_send_packet(self):
        while True:
            if(self.handle_queue.qsize()!=0):
                packet = self.handle_queue.get()
                self.handle_queue.task_done()
                self.send_message(packet)
            time.sleep(0.000001)


    """
    Function used by PacketHandler to put packets meant for android on the handle_queue.
    handle_and_send_packet() should be invoked upon handle_packet().
    """
    def handle_packet(self, packet):
        self.handle_queue.put(packet)

    """
    Function used to wait for a reconnection with Android device
    """
    def reconnect(self):
        self.client_sock.close()
        self.client_sock = None
        UtilsManager.log("LOG", self.id, "waiting for reconnection on RFCOMM channel %d" % self.port)
        self.client_sock, self.client_info = self.server_sock.accept()
        UtilsManager.log("LOG", self.id, "Accepted reconnection from {}".format(self.client_info))
        self.send_message("You are now reconnected to RPI!!")
