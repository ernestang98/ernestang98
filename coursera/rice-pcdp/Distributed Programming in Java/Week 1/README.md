### Map-Reduce Framework

[Computerphile explanation](https://www.youtube.com/watch?v=cvhKoniK5Uo)

[Internet-class explanation](https://www.youtube.com/watch?v=43fqzaSH0CQ)

Map Function:

1. Input data (K:V pairs)

	```
	{
		kA:vA,
		kB:vB,
		...
	}
	```

2. Map each K:V pair of input data into new K:V pairs

	For `kA:vA`

	```
	{
		kA1:vA1,
		kA2:vA2,
		...
	}
	```

3. Group/Shuffle common keys from new K:V pairs into chunks

	```
	{
		k: {vA1, vB1}
	}
	```

Reduce Function:

1. Reduces chunks into single K:V pair

	given reduce function `r() { return val }`

	```
	{
		k: r(vA1, vB1)
	}
	```
	
### Apache Hadoop

Properties:

1. Distributed Map-Reduce

3. Fault tolerant due to distributed storage and parallel processing

Components: 

1. Map Tasks & Reduce Tasks

2. Distributed computers (hence distributed processors and storages) 

3. Scheduler (Schedule tasks to different processors and storages)

4. Query Languages (e.g. Pig, Hive)

Use case: Term Frequency-Inverse Document Frequency (TF-IDF) Weighting

- Information Retrieval Problem: Given a set of documents each being able to be abstracted into terms, find common documents

- Find term frequencies for each document, but some terms are not very useful, such as "the"

- Find document frequencies for each term

- IDF is the inverse of document frequencies, helps to combat the high document frequencies for terms such as "term" as the maximum value would be 1

- Best weight (according to research): WEIGHT(TERMi, DOCUMENTj) = TFij * log( N / DFi )

- Computation of weights can be done easily via Map-Reduce:

	1. Input:  
	
		```
		{
			DOC1: { ... },
			DOC2: { ... },
			...
		}
		```

	2. Map (using DOC1):

		```
		{
			(DOC1, TERM1): 1,
			(DOC1, TERM2): 1,
			(DOC1, TERM2): 1,
			(DOC2, TERM1): 1,
			(DOC2, TERM2): 1,
			...
		}
		```
	
	3. Reduce (obtain TF):

		```
		{
			(DOC1, TERM1): 1,
			(DOC1, TERM2): 2,
			(DOC2, TERM1): 1,
			(DOC2, TERM2): 1,
			...
		}
		```
		
	4. Map (if TF > 0 then 1; else 0)

		```
		{
			TERM1 : 1,
			TERM1 : 1,
			TERM2 : 1,
			TERM2 : 1,
			...
		}
		```
	
	5. Reduce (Reduce all terms to get document frequency for each term - DF)

		```
		{
			TERM1 : 2,
			TERM2 : 2,
			...
		}
		```
		
	

### Apache Spark

Modern version of Apache Hadoop

| | Hadoop  | Spark |
| ------------- | ------------- | ------------- |
| Database Structure | K:V Pairs | RDD  |
| Main Function | Map-Reduce  | General Operations </br> - Transforms (Intermediate), </br>- Actions (Terminal)  |

Additional Properties:

1. Lazy Evaluations (schedule intermediate operations only when you know when the terminal operations will be executed)

2. Caching

Use case: Page Ranking Algorithm

- Given a collection of webpages, find the most important webpage

- RANK(B) = SUM OF ALL ( RANK OF PREDECESSORS OF B / DESTINATION COUNT OF B )

	- Page B is important is many (important) pages point to it

	- Rank determines how important is the page itself

	- Destination count determines how many other pages does a page point to

	- If Page B gets pointed by a page with a lot of outgoing links, then the weightage is much less

- Sequential Algorithm

	```
	for iter : ... {
		
		for each Page A (predecessor) to Page B:
			Calculate Contribution(B) from Page(A)
		
		for each Page B:
			Rank(B) = 0.15 + 0.85 * Contribution(B)
		
	}
	```

- How Spark can help? Caching property

	- First FOR loop can be replaced by `flatMapToPair()`

	- Second FOR loop can be replaced by `reduceByKey()` and `mapValues()`