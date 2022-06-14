"""
Default number of indices: 11
Direct Addressing
Indirect Closed Addressing (Linked List)
Indirect Opened Addressing (Linear Probing)
Indirect Opened Addressing (Double Hashing)
"""


def directAddress(value, hashmap, indices=11):
    if hashmap is None:
        hashmap = {}
    key = value % indices
    try:
        if hashmap[key]:
            print("Collision!")
    except:
        hashmap[key] = value
        return hashmap


def closedAddress(value, hashmap, indices=11):

    key = value % indices
    try:
        if hashmap[key]:
            hashmap[key].append(value)

        return hashmap

    except:
        hashmap[key] = [value]
        return hashmap


def openedAddressLinearProbing(value, hashmap, indices=11):
    key = value % indices
    try:
        if hashmap[key]:
            for i in range(1, 11):
                new_key = (key + i) % 11
                try:
                    if hashmap[new_key]:
                        pass
                except:
                    hashmap[new_key] = value
                    return hashmap

            print("Hashmap full!")
        return hashmap

    except:
        hashmap[key] = value
        return hashmap


def openedAddressDoubleHashing(value, hashmap):
    key = value % 11
    try:
        if hashmap[key]:
            for i in range(1, 11):
                new_key = (key + i * (1 + value % 8)) % 11
                try:
                    if hashmap[new_key]:
                        pass
                except:
                    hashmap[new_key] = value
                    return hashmap

            print("Hashmap full!")
        return hashmap

    except:
        hashmap[key] = value
        return hashmap

