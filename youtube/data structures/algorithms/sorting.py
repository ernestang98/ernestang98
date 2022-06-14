def bubbleSort(array):
    for j in range(len(array)):
        for i in range(len(array) - 1):
            if array[i] > array[i + 1]:
                temp = array[i + 1]
                array[i + 1] = array[i]
                array[i] = temp

    return array


def insertionSort(array):
    for j in range(len(array)):
        while array[j-1] > array[j] and j > 0:
            temp = array[j]
            array[j] = array[j-1]
            array[j-1] = temp
            j -= 1

    return array


def mergeSort(array):
    if len(array) > 1:
        mid = len(array) // 2
        L = array[:mid]
        R = array[mid:]
        mergeSort(L)
        mergeSort(R)

        i = j = k = 0
        while i < len(L) and j < len(R):
            if L[i] < R[j]:
                array[k] = L[i]
                i += 1
            else:
                array[k] = R[j]
                j += 1
            k += 1

        while i < len(L):
            array[k] = L[i]
            i += 1
            k += 1

        while j < len(R):
            array[k] = R[j]
            j += 1
            k += 1

    return array


