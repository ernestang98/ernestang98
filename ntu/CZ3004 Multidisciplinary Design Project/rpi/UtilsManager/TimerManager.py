import time
import serial
import multiprocessing
from UtilsManager import UtilsManager
import threading


class TimerManager(multiprocessing.Process):
    def __init__(self, process_queue, id):
        multiprocessing.Process.__init__(self)
        self.process_queue = process_queue
        self.id = id 
        self.daemon=True
        self.handle_queue = multiprocessing.Manager().Queue()
        self._start = False
        self._force_stop = 350 # worse case scenario whereby the robot car is executing a turn
        self._counter = 0
        self.start()
 
    def run(self):
        UtilsManager.log("INFO", self.id, "TimerManager setup...")

        t1 = threading.Thread(target=self.timer_manager)
        t2 = threading.Thread(target=self.handle_and_send_packet)

        t1.start()
        t2.start()

        t1.join()
        t2.join()


    def timer_manager(self):
        while True:
            if self._start:
                if self._counter >= self._force_stop:
                    UtilsManager.log("INFO", self.id, "Current counter {}, timing going to exceed, cutting all actions now...".format(self._counter))
                    self._start = False
                    self._counter = 0
                    self.process_queue.put("TIM|CAR|TIMER_EXCEED_FORCE_STOP")
                    time.sleep(10)

                else:
                    UtilsManager.log("INFO", self.id, "TimerManager _start is enabled, counter {}".format(self._counter))
                    self._counter += 1
                    time.sleep(1)
            # else:
            #     UtilsManager.log("INFO", self.id, "TimerManager _start is disabled, setting counter to 0")
            #     self._counter = 0
            #     time.sleep(1)


    """
    Called by t1 and when PacketHandler() calls handle_packet, sends packet to Android 
    via client socket
    """
    def handle_and_send_packet(self):
        while True:
            if(self.handle_queue.qsize()!=0):
                packet = self.handle_queue.get().strip()
                self.handle_queue.task_done()
                if packet == "START_TIMER_MANAGER":
                    self._start = True
                elif packet == "STOP_TIMER_MANAGER":
                    self._counter = 0
                    self._start = False


    """
    Function used by PacketHandler to put packets meant for android on the handle_queue.
    handle_and_send_packet() should be invoked upon handle_packet().
    """
    def handle_packet(self, packet):
        self.handle_queue.put(packet)
