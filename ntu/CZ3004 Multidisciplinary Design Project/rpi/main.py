import multiprocessing
from BluetoothManager import BluetoothManager
from USBManager import USBManager
from UtilsManager import TimerManager
from PacketManager import PacketManager
from SocketManager import SocketManager
from VSManager import VSManager
import time


processes = []
process_queue = multiprocessing.Manager().Queue()
packtManager = PacketManager.PacketManager()

btManager = BluetoothManager.BluetoothManager(process_queue, "BTM")
usbManager = USBManager.USBManager(process_queue, "USB")
socketManager = SocketManager.SocketManager(process_queue, "192.168.34.34", 65432, "SOC")
vsManager = VSManager.VSManager(process_queue, "VS")
timerManager = TimerManager.TimerManager(process_queue, "TIM")

packtManager.registerHandler(btManager)
packtManager.registerHandler(usbManager)
packtManager.registerHandler(socketManager)
packtManager.registerHandler(vsManager)
packtManager.registerHandler(timerManager)

processes.append(socketManager)
processes.append(btManager)
processes.append(usbManager)
processes.append(vsManager)
processes.append(timerManager)

# for process in processes:
#     process.start()

while True:
    if(process_queue.qsize()!=0):
        packtManager.handle(process_queue.get())
        process_queue.task_done()
    time.sleep(0.1)

for process in processes:
    process.join()

print('All process ended successfully')
