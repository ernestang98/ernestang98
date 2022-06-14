#!/bin/bash

#sudo hciconfig hci0 piscan
#sudo service bluetooth start
bluetoothctl
discoverable on
agent on
#scan on
default-agent
# sudo rfcomm watch hci0
