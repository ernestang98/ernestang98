import time
import serial
import multiprocessing
from UtilsManager import UtilsManager
import threading


class USBManager(multiprocessing.Process):
    def __init__(self, process_queue, id, debug = False, enable_serial_connection = True):
        multiprocessing.Process.__init__(self)
        self.process_queue = process_queue
        self.id = id 
        self.enable_serial_connection = enable_serial_connection
        self.is_connected = False
        self.debug = debug
        self.serial = None
        self.daemon=True
        self.handle_queue = multiprocessing.Manager().Queue()
        self.hash_map = {
            # "W": ["f", "f010", 2], # forward
            # "A": ["o", "l090", 6], # forward-left
            # "S": ["b", "b010", 2], # backward
            # "D": ["p", "r090", 6], # forward-right
            # "V": ["v", "SEND_TO_SEVER", 15]
            "W": "f",
            "Q": "l",
            "S": "b",
            "E": "r",
            "Z": "k",
            "C": "j",
            "V": "V",
            "O": "o",
            "P": "p",
            "A": "a",
            "D": "d",
            "I": "i"
        }
        self.cache = []
        self.timeout = 12
        self.CA5_FIND_BULLSEYE = False
        self.CA5_FIND_STOP = False
        self.start()

    def test_connection(self):
        try:
            ser = serial.Serial(
                port='/dev/ttyUSB0',
                baudrate = 115200,
                parity=serial.PARITY_NONE,
                stopbits=serial.STOPBITS_ONE,
                bytesize=serial.EIGHTBITS,
                timeout=1
            )
            self.serial = ser
            self.is_connected = True

            if self.debug:
                self.write_message("f100")

            return True
        except:
            return False

 
    def run(self):
        UtilsManager.log("INFO", self.id, "Waiting to connect to Car via USB")
        UtilsManager.log("INFO", self.id, "Debug is {} and Enable_Serial_Connection is {}".format(self.debug, self.enable_serial_connection))
        if self.enable_serial_connection:
            if not self.test_connection():
                UtilsManager.log("ERR", self.id, "Failed to connect via serial USB port, reattempting in 10 seconds...")
                # time.sleep(10000)
                # self.run()
            else:
                t1 = threading.Thread(target=self.read_message)
                t2 = threading.Thread(target=self.handle_and_send_packet)
                # t3 = threading.Thread(target=self.start_CA5)
                
                t1.start()
                t2.start()
                # t3.start()

                t1.join()
                t2.join()
        else:
            t1 = threading.Thread(target=self.read_message)
            t2 = threading.Thread(target=self.handle_and_send_packet)
            # t3 = threading.Thread(target=self.start_CA5)
            
            t1.start()
            t2.start()
            # t3.start()

            t1.join()
            t2.join()


    def write_message(self, message):
        if self.is_connected:
            if self.serial.isOpen():
                message = message.strip()
                self.serial.write(message.encode('raw_unicode_escape')) 
                UtilsManager.log("LOG", self.id, "writing following message to car: " + message)
            else:
                UtilsManager.log("ERR", self.id, "Serial port not open")
        else:
            UtilsManager.log("LOG", self.id, "Sending data to car: " + message)


    def read_message(self):
        if self.enable_serial_connection:
            while True:
                try:
                    data = self.serial.readline().strip().decode('utf-8') #non-blocking
                    if len(data) == 0:
                        continue
                    UtilsManager.log("LOG", self.id, "Received Data From Car: " + data)
                    self.process_queue.put(data)
                except serial.SerialException as e:
                    UtilsManager.log("ERR", self.id, str(e))
                    while self.is_connected == False:
                         UtilsManager.log("LOG", self.id, "Attempting to reconnect to car...")  
                         self.test_connection()
                         time.sleep(3)
                    break
                time.sleep(0.000001)


    def set_cache(self, pkt_list):
        self.cache = pkt_list


    def mutate_cache(self, pkt):
        self.cache.append(pkt)


    def get_cache(self):
        return self.cache


    def start_CA5(self):
        UtilsManager.log("LOG", self.id, "A5 Start!")
        while True:
            if self.CA5_FIND_BULLSEYE:
                UtilsManager.log("INFO", self.id, "Finding bullseye...")
                # self.write_message("f010")
                # time.sleep(2)
                # self.process_queue.put("CAR|IMG|V")
                time.sleep(10)

            if self.CA5_FIND_STOP:
                UtilsManager.log("INFO", self.id, "Found bullseye... Finding stop")
                # L F F, R F, R

                if self.CA5_FIND_STOP:
                    self.write_message("j090")
                    time.sleep(10)

                if self.CA5_FIND_STOP:
                    self.write_message("f059")
                    time.sleep(5)

                if self.CA5_FIND_STOP:  
                    self.write_message("r090")
                    time.sleep(10)

                if self.CA5_FIND_STOP:
                    self.write_message("f005")
                    time.sleep(5)

                if self.CA5_FIND_STOP:
                    self.write_message("r090")
                    time.sleep(10)

                if self.CA5_FIND_STOP:
                    self.process_queue.put("CAR|IMG|V")
                    time.sleep(15)


    """
    Called by t1 and when PacketHandler() calls handle_packet, sends packet to Android 
    via client socket
    """
    def handle_and_send_packet(self):
        while True:
            if(self.handle_queue.qsize()!=0):
                packet = self.handle_queue.get().strip()
                self.handle_queue.task_done()
                if packet == "STMDONE" or packet == "FORCE_TRIGGER_NEXT":
                    if self.cache != []:
                        UtilsManager.log("INFO", self.id, "Cache: {}".format(self.cache))
                        curr = self.cache.pop(0)
                        self.sp_send_packet(curr)
                    else:
                        UtilsManager.log("INFO", self.id, "Cache is empty! Making sure that timer manager stops...")
                        self.process_queue.put("CAR|TIM|STOP_TIMER_MANAGER")

                elif packet == "CH2":
                    UtilsManager.log("INFO", self.id, "Start challenge 2!")
                    self.write_message("ww69")

                elif packet == "MESSED_UP":
                    UtilsManager.log("INFO", self.id, "We messed up, force restarting and clearing cache...")
                    curr = self.cache
                    self.set_cache([])
                    UtilsManager.log("INFO", self.id, "Cleared {} to {}".format(curr, self.cache))


                elif packet == "CA5":
                    self.write_message("i000")
                    self.CA5_FIND_BULLSEYE = True
                elif packet == "A5_DONE":
                    self.process_queue.put("CAR|IMG|V")
                    time.sleep(10)
                    # time.sleep(15)
                    self.CA5_FIND_BULLSEYE = False
                    self.CA5_FIND_STOP = True
                elif packet == "CA5_STOP":
                    self.CA5_FIND_STOP = False
                    UtilsManager.log("INFO", self.id, "Found stop... A5 completed!")


                elif packet == "TIMER_EXCEED_FORCE_STOP":
                    initial = self.cache
                    self.set_cache([])
                    UtilsManager.log("INFO", self.id, "Force clear cache from {} to {}".format(initial, self.cache))


                else:
                    if len(packet) > 5: # check if it could be a SP_ instruction
                        if packet[:3] == "SP_": # check if it is a SP_ instruction
                            UtilsManager.log("INFO", self.id, "Before concatenation: {}".format(packet[3:]))
                            packet_list = packet[3:].split(",") # [W, W, W, A, W, A, S, S, A, D]
                            packet_list = self.reformat_packet_list(packet_list)
                            UtilsManager.log("INFO", self.id, "After concatenation: {}".format(packet_list))

  

                            self.set_cache(packet_list)

                            curr = self.cache.pop(0)
                            self.sp_send_packet(curr)
                            self.process_queue.put("CAR|TIM|START_TIMER_MANAGER")
                        else:
                            self.write_message(packet)
                    else:
                        self.write_message(packet)
            time.sleep(0.000001)


    def sp_send_packet(self, pkt):
        UtilsManager.log("LOG", self.id, "time now (for optimizing delay)".format(pkt)) 
        pkt = "".join(pkt.split()).strip()
        pkt_dir = pkt[0]
        if pkt_dir not in self.hash_map:
            UtilsManager.log("LOG", self.id, "Invalid instruction {} to be converted".format(pkt)) 
        else:
            if pkt_dir == "V":
                self.process_queue.put("ALG|IMG|{}".format(pkt_dir))
                time.sleep(self.timeout)
                self.process_queue.put("ALG|IMG|{}".format("STOP_PI_CAM"))
                self.process_queue.put("STMDONE")
            else:
                if pkt_dir == "W" or pkt_dir == "S":
                    pkt_num = int(pkt[1:]) * 10
                else:
                    pkt_num = int(pkt[1:]) * 90

                if pkt_num < 100:
                    pkt_num = str(0) + str(pkt_num)
                else:
                    pkt_num = str(pkt_num)

                # final_pkt = self.hash_map[pkt_dir][0] + pkt_num
                final_pkt = self.hash_map[pkt_dir] + pkt_num   
                self.process_queue.put("Sending instruction {} to car".format(final_pkt))
                self.write_message(final_pkt)


    def reformat_packet_list(self, packet_list):

        if len(packet_list) == 1:
            return [packet_list[0] + str(1)]

        lst = []
        cache_digit = 1
        cache_string = ""
        prev = packet_list[0]

        for i in range(1, len(packet_list)):
            curr = packet_list[i]
            if prev == curr:
                cache_digit += 1
            else:
                cache_string += (prev + str(cache_digit))
                lst.append(cache_string)
                cache_digit = 1
                cache_string = ""
            prev = curr

        if prev != "":
            cache_string += (prev + str(cache_digit))
            lst.append(cache_string)

        return lst


    """
    Function used by PacketHandler to put packets meant for android on the handle_queue.
    handle_and_send_packet() should be invoked upon handle_packet().
    """
    def handle_packet(self, packet):
        self.handle_queue.put(packet)
