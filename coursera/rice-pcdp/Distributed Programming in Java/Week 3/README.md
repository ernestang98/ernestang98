### Single Program Multiple Data Model

Enables distributed parallelism

Cluster of distributed node can be initialised as a single parallel computer

- Each node will have its own processor, local memory, and network interface card

- Data is distributed across the nodes (Data Distribution)

- Data to be distributed has a `global view` logical data structure

- Each node receives a `local view` of a piece of the data and it is stored in its own local memory

- Developers need to map the logical `global view` and `local views` 

- Tl;dr run same program with different data inputs on different computer nodes

How does it work?

- Message Passing Interface (MPI)

- Assign fixed set of processes to participate

- Each node will usually execute one MPI process

- Each process starts its own copy of MPI program

- [More here](https://slideplayer.com/slide/7559656/)

Other things to note:

- RANK = node id number for this section of course material

### P2P Communuication

- Allows 2 nodes to communicate with one another

- Basic Commands:

	- `.RECV()`

	- `.SEND()`

	- `.FINALIZE()`

- Properties:

	- Message Ordering, for 2 given messages to be sent and received M1 & M2:
	
		- No guarantee of order fairness when we are using independent sets of senders and receivers, which makes sense in a huge distributed system
		
		- Same order of messages sent is guaranteed if:
	
			1. Same sender
			
			2. Same receiver
			
			3. Same data type
			
			4. Same tag  
		
	- Deadlock
	
		- Sending and receiving functions are blocking operators
	
		- Deadlocks are possible in SPMD programs
	
		- Solution:
	
			1. Note order of function calls
			
			2. Use `.SendRecv()` in MPI API which avoids deadlocks

### Non-Blocking Communication

- Allows 2 nodes to communicate with one another

- Basic Commands:

	- `.IRECV()`

	- `.ISEND()`

	- `.WAIT()`

- `I` stands for immediate, does not get blocked

- Properties:

	- No deadlock

	- Increases parallelism and throughput (reduces idle time)

### Collective Communication

- Allows a node to easily communicate to multiple nodes (something like broadcasting in computer networks)

- Basic Commands:

	- `.BCAST()`

	- `.REDUCE()`

- Similar to Map-Reduce
