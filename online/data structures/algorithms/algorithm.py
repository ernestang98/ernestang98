def kadaneAlgo(array):
    endIndex = 0
    currMax = array[0]
    globalMax = array[0]

    for i in range(1, len(array)):

        currMax = max(array[i], currMax + array[i])

        if currMax > globalMax:
            globalMax = currMax
            endIndex = i

    startIndex = endIndex

    while startIndex >= 0:
        globalMax -= array[startIndex]

        if globalMax == 0:
            break

        startIndex -= 1

    for i in range(startIndex, endIndex + 1):
        print(array[i], end=" ")

    print()
