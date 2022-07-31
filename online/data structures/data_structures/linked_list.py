# CS Dojo https://www.youtube.com/watch?v=RBSGKlAvoiM
# h -> n0 -> n1 -> n2 -> null

class Node:
    def __init__(self, data=None):
        self.data = data
        self.nextNode = None

    def insertNode(self, inserted_node):
        if self.nextNode is not None:
            oldNextNode = self.nextNode
            self.nextNode = inserted_node
            inserted_node.nextNode = oldNextNode
        else:
            self.nextNode = inserted_node
        return inserted_node


class singlyLinkedList:
    def __init__(self):
        self.head = None

    def printLinkedList(self):
        currentNode = self.head
        while currentNode is not None:
            print(currentNode.data, end="->")
            currentNode = currentNode.nextNode

        print("null")

    def reverseLinkedList(self):
        previousNode = None
        currentNode = self.head

        # reversing linked list
        # null    h -> n0 -> n1 -> n2 -> null
        # null <- h    n0 -> n1 -> n2 -> null
        # null <- h <- n0    n1 -> n2 -> null
        # null <- h <- n0 <- n1    n2 -> null
        while currentNode is not None:
            followingNode = currentNode.nextNode
            currentNode.nextNode = previousNode
            previousNode = currentNode
            currentNode = followingNode

        # printing reversed linked list
        currentNode = previousNode
        while currentNode is not None:
            print(currentNode.data, end="->")
            currentNode = currentNode.nextNode

        print("null")


class doublyNode:
    def __init__(self, data=None):
        self.data = data
        self.nextNode = None
        self.prevNode = None


class doublyLinkedList:
    def __init__(self):
        self.head = None

    def printLinkedList(self):
        currentNode = self.head
        while currentNode is not None:
            if currentNode.nextNode is None:
                print(currentNode.data, end="->")
            else:
                print(currentNode.data, end="<->")
            currentNode = currentNode.nextNode

        print("null")

    def printLinkedListBackwards(self):
        currentNode = self.head
        while currentNode.nextNode is not None:
            currentNode = currentNode.nextNode

        while currentNode is not None:
            if currentNode.prevNode is None:
                print(currentNode.data, end="->")
            else:
                print(currentNode.data, end="<->")
            currentNode = currentNode.prevNode

        print("null")
