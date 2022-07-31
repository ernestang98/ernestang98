"""
Dynamic Programming Test
"""

# from algorithms.algorithm import kadaneAlgo

# kadaneAlgo([1, -2, 3, 7, -6, 5, 3, -20, 10])


"""
Hashing Test
"""

# from algorithms.utils import generateHashMapForDirectAddressing, printClosedAddressingHashMap, \
#     printClosedAddressingHashMapKey, generateHashMapForClosedAddressing, \
#     generateHashMapForOpenedAddressingLinearProbing, generateHashMapForOpenedAddressingDoubleHashing

# generateHashMapForDirectAddressing()
# printClosedAddressingHashMap(generateHashMapForClosedAddressing(), 10)
# printClosedAddressingHashMapKey(generateHashMapForClosedAddressing(), 9)
# printClosedAddressingHashMapKey(generateHashMapForClosedAddressing(), 4)
# generateHashMapForOpenedAddressingLinearProbing()
# generateHashMapForOpenedAddressingDoubleHashing()


"""
Array Test
"""

# from algorithms.utils import generateArray
# from algorithms.sorting import bubbleSort
# from algorithms.searching import linearSearch, binarySearch
#
# print(generateArray())
# print(linearSearch(3, generateArray()))
# print(linearSearch(0, generateArray()))
# print(linearSearch(2, generateArray()))
#
# array = bubbleSort(generateArray())
#
# print(array)
# print(binarySearch(3, array[0], array[len(array) - 1], array))
# print(binarySearch(0, array[0], array[len(array) - 1], array))
# print(binarySearch(2, array[0], array[len(array) - 1], array))


"""
Binary Tree Test
            1
         /    |
       5       6
     /  |    /  |
    7   2   9  Null
"""

# from algorithms.utils import generateBinaryTree
# from data_structures import binary_tree
#
# # 1 5 7 2 6 9
# print("Preorder traversal of binary tree is: ")
# binary_tree.printPreorder(generateBinaryTree())
# print("\n")
#
# # 7 5 2 1 6
# print("Inorder traversal of binary tree is: ")
# binary_tree.printInorder(generateBinaryTree())
# print("\n")
#
# # 7 2 5 6 1
# print("Postorder traversal of binary tree is: ")
# binary_tree.printPostorder(generateBinaryTree())
# print("\n")


"""
Linked List Test
"""

from algorithms.utils import generateSinglyLinkedList, generateDoublyLinkedList

ll = generateSinglyLinkedList()

ll.printLinkedList()
ll.reverseLinkedList()

dll = generateDoublyLinkedList()

dll.printLinkedList()
dll.printLinkedListBackwards()
