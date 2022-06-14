import logging
import struct
import os
from UtilsManager import UtilsManager

#this class is to check the data recieved from any devices that was created and linked in.
#Data format required to be in "<Self.Header>:<Reciever.Header>:Data"

class PacketManager:
    
    def __init__(self):
        self.id = "PKT"      
        self.handlers = {}
        self.hash_map = {
            "AND": "BTM",
            "CAR": "USB",
            "ALG": "SOC",
            "IMG": "VS",
            "TIM": "TIM"
        }


    def registerHandler(self,instance):
        unique_id = instance.id
        if unique_id not in self.handlers:
            self.handlers[unique_id] = instance
        else:
            print("Failed to register handler, please choose another id")


    def unregisterHandler(self,unique_id):
        try:
            del self.handlers[unique_id]
        except KeyError:
            print("Fail to remove, handler not found.")

            
    def handle(self,packet):
        """
        Special Case 1: STM -> RPI "STMDONE"
        """
        if packet == "STMDONE" or packet == "A5_DONE":
            UtilsManager.log("LOG", self.id, "Received special string: {}".format(packet))
            self.handlers["USB"].handle_packet(packet)

        else:
            split_packet = packet.split('|')

            if len(split_packet) >= 3:
                SENDER = "".join(split_packet[0].split()).strip()
                RECEIVER = "".join(split_packet[1].split()).strip()
                MESSAGE = "".join(split_packet[2].split()).strip()

                # if SENDER == "IMG" and MESSAGE != "NO":
                #     self.handlers["USB"].handle_packet("FORCE_TRIGGER_NEXT")
                    #self.handlers["VS"].handle_packet("STOP_PI_CAM")

                if "," in RECEIVER:
                    RECEIVER_ARRAY = RECEIVER.split(",")
                    UtilsManager.log("INFO", self.id, "Splitting {} into {}".format(RECEIVER, RECEIVER_ARRAY))
                    for RECEIVER_ELEMENT in RECEIVER_ARRAY:
                        if RECEIVER_ELEMENT not in self.hash_map:
                            UtilsManager.log("ERR", self.id, "Unknown receiver {}, sent by {}".format(RECEIVER_ELEMENT, SENDER))
                        else:
                            RECEIVER_ID = self.hash_map[RECEIVER_ELEMENT]
                            if RECEIVER_ID in self.handlers:
                                UtilsManager.log("LOG", self.id, "Received {} from {}, to be sent to {}".format(MESSAGE, SENDER, RECEIVER_ELEMENT))
                                self.handlers[RECEIVER_ID].handle_packet(MESSAGE)

                else:
                    if RECEIVER not in self.hash_map:
                        UtilsManager.log("ERR", self.id, "Unknown receiver {}, sent by {}".format(RECEIVER, SENDER))
                    else:
                        RECEIVER_ID = self.hash_map[RECEIVER]
                        if RECEIVER_ID in self.handlers:
                            UtilsManager.log("LOG", self.id, "Received {} from {}, to be sent to {}".format(MESSAGE, SENDER, RECEIVER))
                            self.handlers[RECEIVER_ID].handle_packet(MESSAGE)

            else:
                UtilsManager.log("ERR", self.id, "Received {}, not correctly formatted!".format(packet))

          
