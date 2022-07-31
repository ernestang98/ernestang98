class treeNode:
    def __init__(self, data=None):
        self.data = data
        self.children = []


root = treeNode(5)
node0 = treeNode(1)
node1 = treeNode(9)
node2 = treeNode(2)
node3 = treeNode(7)

root.children = [node0, node1, node2]

node2.children = [node3]


# tree, truck, air
class trieNode:
    def __init__(self, char=None):
        self.char = char
        self.children = []
        self.word = False


root = trieNode()
node0 = trieNode("t")
node1 = trieNode("r")

node2 = trieNode("e")
node3 = trieNode("e")

node4 = trieNode("u")
node5 = trieNode("c")
node6 = trieNode("k")

node7 = trieNode("a")
node8 = trieNode("i")
node9 = trieNode("r")

root.children = [node0, node7]

node0.children = [node1]

node1.children = [node2, node4]

node2.children = [node3]
node3.word = True

node4.children = [node5]
node5.children = [node6]
node6.word = True

node7.children = [node8]
node8.children = [node9]
node9.word = True
