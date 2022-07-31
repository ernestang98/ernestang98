def linearSearch(to_find, array):
    for i in range(len(array)):
        if array[i] is int(to_find):
            return i

    return -1


def binarySearch(to_find, start, end, array):
    if start < end:
        mid = int((start + end) / 2)
        if array[mid] is to_find:
            return mid

        elif array[mid] < to_find:
            return binarySearch(to_find, mid + 1, end, array)

        else:
            return binarySearch(to_find, start, mid - 1, array)

    else:
        return -1

