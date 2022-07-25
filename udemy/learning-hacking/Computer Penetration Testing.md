# Computer Penetration Testing

### Section 1: Server-Side Attacks

- `nmap/zenmap` for reconnaissance

- `metasploit` to exploit vulnerabilities and obtain root

- `nexpose` to scan for vulnerabilities and produce report

### Section 2: Client-Side Attacks - Payload Generation

- `veil` to generate undetectable payloads (something like `msfvenom`)

- Check if payload is detectable by AV on any of these [websites](https://zsecurity.org/forums/topic/no-distribute-alternative/)

- Use `metasploit` or `nc` to listen for connections

### Section 2: Client-Side Attacks - Payload Delivery

- Host on webserver

- Backdoor upgrades via `evilgrade`

  - Requires MITM Attack

  - Requires client to attempt to upgrade software

- Backdoor downloads via `bdfproxy`

  - Requires MITM Attack

  - Requires client to attempt to download and run/open files from websites without HTTPS

- Social Engineering

  - maltego

  - Generating a backdoor in the form of normal files (e.g. jpg, pdf) with seemingly legitimate icon

    - AutoIt script

    - Au2exe Compiler

  - Delivering payload via email

    - [Sendinblue](https://www.sendinblue.com/) mail server
    
    - [sendmail](https://www.kali.org/tools/sendemail/) tool

    - php + web hosting server such as [dreamhost](https://www.dreamhost.com/)

  - BeEF

    - Delivering payload via webserver

    - Delivering payload via bettercap

### Section 3: Client-Side Attacks - Outside Local Network

- [How](https://apple.stackexchange.com/questions/418733/is-it-possible-to-use-my-public-ip-address-to-serve-a-website-catalina) to use public ip to serve web server (general guide).

- [How](https://stackoverflow.com/questions/22730420/how-to-configure-apache-webserver-to-be-accessed-by-public-domain-or-static-ip) to use router port forwarding to use public ip to serve web server.

- [How](https://www.youtube.com/watch?v=N8f5zv9UUMI) to use SSH tunnelling to perform port forwarding to use public ip to serve web server

- Idea is that if you can serve your private webserver on public ip which is reachable from anywhere on internet (try `ping 8.8.8.8` to prove this), then you can not only deliver your payload but also receive back connections and a shell.

- [How](https://www.youtube.com/watch?v=rjiUsyQTaQE) to obtain a reverse shell from external network.

### Section 4: Server-Side/Client-Side Attacks - Post exploitation

- Looting

- Maintaining Access using Metasploit

- Pivoting using Metasploit Autoroute

- Keylogging, Screenshoting etc. using Metasploit