from algorithms.hashing import directAddress, closedAddress, openedAddressLinearProbing, openedAddressDoubleHashing
from data_structures.binary_tree import Node as BTNode
from data_structures.linked_list import singlyLinkedList, Node as LLNode, doublyLinkedList, doublyNode as DLLNode
import random

random.seed(10)


def generateBinaryTree():

    #           1
    #        /     \
    #       5       6
    #     /  \     /  \
    #    7    2   9   Null

    node0 = BTNode(1)
    node1 = BTNode(5)
    node2 = BTNode(6)
    node3 = BTNode(7)
    node4 = BTNode(2)
    node5 = BTNode(9)

    node0.left = node1
    node0.right = node2

    node1.left = node3
    node1.right = node4

    node2.left = node5

    root = node0

    return root


def generateArray():
    array = []
    for i in range(10):
        array.append(random.randint(0, 10))
    return array


def generateSinglyLinkedList():
    ll = singlyLinkedList()

    ll.head = LLNode(5)
    node0 = LLNode(2)
    node1 = LLNode(6)
    node2 = LLNode(1)
    node3 = LLNode(8)

    ll.head.nextNode = node0
    node0.nextNode = node1
    node1.nextNode = node2
    node2.nextNode = node3

    return ll


def generateDoublyLinkedList():
    dll = doublyLinkedList()
    dll.head = DLLNode(3)
    doublyNode0 = DLLNode(1)
    doublyNode1 = DLLNode(6)
    doublyNode2 = DLLNode(7)
    doublyNode3 = DLLNode(9)

    dll.head.nextNode = doublyNode0
    doublyNode0.nextNode = doublyNode1
    doublyNode0.prevNode = dll.head
    doublyNode1.nextNode = doublyNode2
    doublyNode1.prevNode = doublyNode0
    doublyNode2.nextNode = doublyNode3
    doublyNode2.prevNode = doublyNode1
    doublyNode3.prevNode = doublyNode2

    return dll


# doublyLinkedList.printLinkedList()
# doublyLinkedList.printLinkedListBackwards()


def generateHashMapForDirectAddressing():
    hashMap = {}
    directAddress(20, hashMap)
    directAddress(31, hashMap)
    directAddress(15, hashMap)
    directAddress(4, hashMap)
    return hashMap


def generateHashMapForClosedAddressing():
    hashMap = {}
    closedAddress(20, hashMap)
    closedAddress(31, hashMap)
    closedAddress(42, hashMap)
    closedAddress(15, hashMap)
    closedAddress(4, hashMap)
    print(hashMap)
    return hashMap


def printClosedAddressingHashMap(hashmap, indices):
    for a in range(indices):
        try:
            linkedList = hashmap[a]
            for i in range(len(linkedList)):
                print(linkedList[i], end="->")

            print("null")
        except:
            print("Empty")


def printClosedAddressingHashMapKey(hashmap, key):
    try:
        linkedList = hashmap[key]
        for i in range(len(linkedList)):
            print(linkedList[i], end="->")

        print("null")
    except:
        print("Empty")


def generateHashMapForOpenedAddressingLinearProbing():
    hashMap = {}
    openedAddressLinearProbing(20, hashMap)
    openedAddressLinearProbing(31, hashMap)
    openedAddressLinearProbing(42, hashMap)
    openedAddressLinearProbing(53, hashMap)
    openedAddressLinearProbing(15, hashMap)
    openedAddressLinearProbing(4, hashMap)
    openedAddressLinearProbing(1, hashMap)
    openedAddressLinearProbing(1, hashMap)
    openedAddressLinearProbing(1, hashMap)
    openedAddressLinearProbing(1, hashMap)
    openedAddressLinearProbing(1, hashMap)
    openedAddressLinearProbing(100, hashMap)
    print(hashMap)


def generateHashMapForOpenedAddressingDoubleHashing():
    hashMap = {}
    openedAddressDoubleHashing(20, hashMap)
    openedAddressDoubleHashing(31, hashMap)
    openedAddressDoubleHashing(42, hashMap)
    openedAddressDoubleHashing(53, hashMap)
    openedAddressDoubleHashing(15, hashMap)
    openedAddressDoubleHashing(4, hashMap)
    openedAddressDoubleHashing(1, hashMap)
    openedAddressDoubleHashing(1, hashMap)
    openedAddressDoubleHashing(1, hashMap)
    openedAddressDoubleHashing(1, hashMap)
    openedAddressDoubleHashing(1, hashMap)
    openedAddressDoubleHashing(100, hashMap)
    print(hashMap)
