import heapq

def task1(startNode, endNode, G, cost, dist, coord):
    print("Running Task 1, please wait...")
    print()

    """
    Elements in openHeap will contain the following format:
    (totalDistance, totalEnergyCost, node)
    """
    openHeap = []

    """
    Elements in openDict will contain the following format:
    node: (totalDistance, totalEnergyCost, parentNode)
    """
    openDict = {}

    """
    Elements in visitedDict will contain the following format:
    node: (totalDistance, totalEnergyCost, parentNode)
    """
    visitedDict = {}

    """
    Push startNode into the heap, with totalDistance = 0 and totalEnergyCost = 0
    """
    heapq.heappush(openHeap, (0, 0, startNode))
    openDict[startNode] = (0, 0, startNode)

    while openHeap:
        # Choose the node with the lowest totalDistance inside the heap
        chosenNode = openHeap[0][2]
        totalDistance = openHeap[0][0]
        totalEnergyCost = openHeap[0][1]
        
        # Add the chosenNode into the visitedDict
        visitedDict[chosenNode] = openDict[chosenNode]
        
        # Pop the chosenNode from the heap
        heapq.heappop(openHeap)
            
        # Check if we have reached the goal
        if chosenNode == endNode:
            # Backtrack to find the path
            reversedPath = [chosenNode]
            tmp = chosenNode
            while True:
                parentNode = visitedDict[tmp][2]
                reversedPath.append(parentNode)
                
                if parentNode == startNode:
                    break
                
                tmp = parentNode
                
            path = reversedPath[::-1]
                
            print("Shortest path: ", "->".join(path))
            print("Shortest distance: ", totalDistance)
            print("Total energy cost: ", totalEnergyCost)
                        
            break
        
        # Expand the chosenNode
        for neighbourNode in G[chosenNode]:
            distFromChosenToNeighbour = dist[f'{chosenNode},{neighbourNode}']
            totalDistFromStartToNeighbour = distFromChosenToNeighbour + totalDistance
            
            energyCostFromChosenToNeighbour = cost[f'{chosenNode},{neighbourNode}']
            totalEnergyCostFromStartToNeighbour = energyCostFromChosenToNeighbour + totalEnergyCost
            
            """
            Check whether the neighbourNode already exists in openDict; if it does, check whether the totalDistFromStartToNeighbour 
            to that neighbourNode is lower from the current path or from that path
            """
            skip = 0
            if neighbourNode in openDict:
                if totalDistFromStartToNeighbour > openDict[neighbourNode][0]:
                    skip = 1
            
            """
            If neighbourNode does not exists in openDict or totalDistFromStartToNeighbour is lower from the currentPath, add into
            the heap
            """
            if skip == 0:
                heapq.heappush(openHeap, (totalDistFromStartToNeighbour, totalEnergyCostFromStartToNeighbour, neighbourNode))
                openDict[neighbourNode] = (totalDistFromStartToNeighbour, totalEnergyCostFromStartToNeighbour, chosenNode)