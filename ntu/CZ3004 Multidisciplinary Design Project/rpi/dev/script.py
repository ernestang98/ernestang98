import time

def reformat_packet_list(packet_list):

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

print(reformat_packet_list(["W"]))

print(reformat_packet_list(["W", "W", "W", "A", "W"]))

print(reformat_packet_list(["W", "W", "W", "W", "W"]))

print(reformat_packet_list(["W", "W", "W", "W", "A"]))

print(reformat_packet_list(["W", "W", "W", "W", "A", "A"]))

print(reformat_packet_list(["W", "W", "W", "W", "A", "A", "S", "D", "W", "W"]))


packet_list = reformat_packet_list(["W", "W", "W", "W", "A", "A", "S", "D", "W", "W"])

hash_map = {
    "W": ["f", "f010", 2], # forward
    "A": ["l", "l090", 6], # forward-left
    "S": ["b", "b010", 2], # backward
    "D": ["r", "r090", 6], # forward-right
    "V": ["v", "SEND_TO_SERVER", 15]
}

def test():

    packet_list = reformat_packet_list(["W", "W", "W", "W", "A", "A", "W", "W"])

    print("_____ TEST STARTING _____")
    print(packet_list)

    hash_map = {
        "W": ["f", "f010", 2], # forward
        "A": ["l", "l090", 6], # forward-left
        "S": ["b", "b010", 2], # backward
        "D": ["r", "r090", 6], # forward-right
        "V": ["v", "SEND_TO_SERVER", 15]
    }

    for pkt in packet_list:
        pkt = "".join(pkt.split()).strip()
        pkt_dir = pkt[0]
        if pkt_dir not in hash_map:
            print("NOT IN HASH_MAP")
        else:
            if pkt_dir == "V":
                print("I AM V")
                time.sleep(hash_map[pkt_dir][2]/2)
            else:
                cached_counter = int(pkt[1:])
                if pkt_dir == "W" or pkt_dir == "S":
                    pkt_num = int(pkt[1:]) * 10
                else:
                    pkt_num = int(pkt[1:]) * 90
                if pkt_num < 100:
                    pkt_num = str(0) + str(pkt_num)
                else:
                    pkt_num = str(pkt_num)

                final_pkt = hash_map[pkt_dir][0] + pkt_num
                print(final_pkt)
                time.sleep(hash_map[pkt_dir][2]/2 * cached_counter)

    print("_____ TEST COMPLETED _____")



test()
