import json
import argparse
from Task1 import task1
from Task2 import task2
from Task3 import task3

if __name__ == '__main__':

	startNode = '1'
	endNode = '50'
	energyBudget = 287932

	with open('Coord.json') as f:
	    coord = json.loads(f.read())
	    
	with open('Cost.json') as f:
	    cost = json.loads(f.read())
	    
	with open('Dist.json') as f:
	    dist = json.loads(f.read())
	    
	with open('G.json') as f:
	    G = json.loads(f.read())


	try:    
		parser = argparse.ArgumentParser()
		parser.add_argument('--task', type=int, choices=[1, 2, 3], required=True)
		args = parser.parse_args()

		if args.task == 1:
			task1(startNode, endNode, G, cost, dist, coord)
		elif args.task == 2:
			task2(startNode, endNode, energyBudget, G, cost, dist, coord)
		elif args.task == 3:
			task3(startNode, endNode, energyBudget, G, cost, dist, coord)

	except:
		task1(startNode, endNode, G, cost, dist, coord)
		task2(startNode, endNode, energyBudget, G, cost, dist, coord)
		task3(startNode, endNode, energyBudget, G, cost, dist, coord)