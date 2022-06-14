# this is the main app 
import multiprocessing
from Poc import Poc
#from BluetoothMgmt import BluetoothMgmt
from ImageMgmt import ImageMgmt
#from StmMgmt import StmMgmt

processes = []
process_queue = multiprocessing.Manager().Queue()

p = Poc.Poc(process_queue)
i = ImageMgmt.ImageMgmt(process_queue)
#b = BluetoothMgmt.BluetoothMgmt(process_queue)

#processes.append(b)
processes.append(i)
processes.append(p)

for process in processes:
    process.start()

for process in processes:
    process.join()
