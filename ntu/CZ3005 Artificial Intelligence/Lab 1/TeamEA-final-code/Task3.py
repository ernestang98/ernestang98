import heapq
import math

"""
This function returns the euclidean distance between two points, used as the heuristic function for task 3
@param coord1: a tuple (x, y) representing a coordinate
@param coord2: a tuple (x, y) representing a coordinate
"""
def eucDist(coord1, coord2):
    return math.sqrt((coord1[0] - coord2[0]) ** 2 + (coord1[1] - coord2[1]) ** 2)

def task3(startNode, endNode, energyBudget, G, cost, dist, coord):
	print("Running Task 3, please wait...")
	print()

	"""
	Elements in openHeap will contain the following format:
	(totalDistance + eucDist to endNode, totalEnergyCost, node)
	"""
	openHeap = []

	"""
	Elements in openDict will contain the following format:
	node: (totalDistance + eucDist to endNode, totalEnergyCost, parentNode)
	"""
	openDict = {}

	"""
	Elements in visitedDict will contain the following format:
	node: (totalDistance + eucDist to endNode, totalEnergyCost, parentNode)
	"""
	visitedDict = {}

	"""
	Push startNode into the heap, with totalDistance + eucDist to endNode = eucDist(coordinates of startNode, coordinates of endNode) 
	and totalEnergyCost = 0
	"""
	heapq.heappush(openHeap, (eucDist(coord[startNode], coord[endNode]), 0, startNode))
	openDict[startNode] = (eucDist(coord[startNode], coord[endNode]), 0, startNode)

	while openHeap:
	    # Choose the node with the lowest totalDistance inside the heap
	    chosenNode = openHeap[0][2]
	    totalEnergyCost = openHeap[0][1]
	    # Subtract away eucDist to get the totalDist
	    totalDistance = openHeap[0][0] - eucDist(coord[chosenNode], coord[endNode])
	    
	    # Add the chosenNode into the visitedDict
	    visitedDict[chosenNode] = openDict[chosenNode]
	    
	    # Pop the chosenNode from the heap
	    heapq.heappop(openHeap)
	    
	    # Check if we have reached the goal
	    if chosenNode == endNode:

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
	        energyCostFromChosenToNeighbour = cost[f'{chosenNode},{neighbourNode}']
	        
	        # Other than the distance cost, we need to add the euclidean distance as the heuristics cost
	        totalDistFromStartToNeighbour = totalDistance + distFromChosenToNeighbour + eucDist(coord[neighbourNode], coord[endNode])
	        totalEnergyCostFromStartToNeighbour = totalEnergyCost + energyCostFromChosenToNeighbour
	        
	        # Additional constraint that we need to check: the shortest path must satisfy the energyBudget constraint 
	        if totalEnergyCostFromStartToNeighbour <= energyBudget:
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
	    