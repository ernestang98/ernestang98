# MDP Group 34 RPI Repository

### Set up

1. Plug in the RPI using power cable (do not use any other USB C cable, RPI will not power on as insufficient power)

2. SSH pi@192.168.34.34 (have 2 terminals)

    i. FIRST TERMINAL (reference bt.sh)

      ```
      bluetoothctl
      discoverable on
      agent on
      defualt-agent
      ``` 
      
    ii. SECOND TERMINAL
    
      ```
      cd shared/MDP
      sudo -E python3 main.py
      ```
      
3. Check devnotes.txt for more information should you encounter difficulties

### Connect BT Device for Android (Bug - unresolved for now)

1. After you set up the FIRST TERMINAL, connect your BT device

2. Run the script on the SECOND TERMINAL, log should be RFCOMM 1

3. Disconnect and reconnect your BT device

4. You may have to update parameters in the BluetoothManager.py script

### Connect Socket for Algo

1. Go do `dev/`

2. run java Client after you have set up the RPI (use this only if you don't have the actual algo program)

3. You may have to update parameters in the SocketManager.py script

### Connect USB for Robot Car

1. Plug in USB cable to robot car

2. Should auto connect when you run main.py

3. You may have to update parameters in the USBManager.py script

### Connect TCP for Image Recognition

1. Should auto connect when you run main.py

2. You may have to update parameters in the VSManager.py script







