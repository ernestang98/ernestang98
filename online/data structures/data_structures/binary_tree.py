"""
1. Preorder is go one round anticlockwise from the root and print every value
2. Inorder is go one round anticlockwise from the root and print every value
on the second visit (after the left branch is explored)
3. Postorder is go one round anticlockwise from the root and print every value
on the third visit (after both left and right branch is explored)

https://www.youtube.com/watch?v=WLvU5EQVZqY
"""


class Node:
    def __init__(self, key):
        self.left = None
        self.right = None
        self.val = key

    def PrintTree(self):
        print(self.val)
        if self.left:
            self.left.PrintTree()
        if self.right:
            self.right.PrintTree()


# A function to do inorder tree traversal
def printInorder(root):
    if root:
        # First recur on left child
        printInorder(root.left)

        # then print the data of node
        print(root.val, end=" ")

        # now recur on right child
        printInorder(root.right)


# A function to do postorder tree traversal
def printPostorder(root):
    if root:
        # First recur on left child
        printPostorder(root.left)

        # the recur on right child
        printPostorder(root.right)

        # now print the data of node
        print(root.val, end=" ")


# A function to do preorder tree traversal
def printPreorder(root):
    if root:
        # First print the data of node
        print(root.val, end=" ")

        # Then recur on left child
        printPreorder(root.left)

        # Finally recur on right child
        printPreorder(root.right)

