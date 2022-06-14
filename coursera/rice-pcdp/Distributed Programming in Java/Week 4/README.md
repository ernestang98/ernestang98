### Process and Threads

- In Java, a process is a JVM

- Each process has:

    - Process Resources

    - \>= 1 Thread

    - Each thread will share the process resources

- Maps well to computer hardware used in distributed systems, made of multiple nodes

- Each node has:

    - Local memory

    - Network Interface Card

    - \>= 1 processors/cores
    
    - A thread of a process can run only at one core of a node at any point of time 

Benefits of multiple threads in a process

- Memory/Resource Sharing

- Responsiveness (network delays)

- Performance (higher throughput)

Benefits of multiple processes in a node:

- Responsiveness (JVM delays)

- Scalability (only so much more throughput that multithreaded can bring about)

- Availability/Resilience (if 1 process goes down, there are still other processes running and service can still run)

Overall:

- Processes are the basic units of distribution. We can distribute processes across multiple nodes in a data center and even create multiple processes, within a node.

- Threads are the basic unit of parallelism and concurrency. We can have multiple threads in a process that can share resources like memory, and provide performance, can improve responsiveness, can be more efficient in terms of sharing memory. But their ability is limited in terms of scalability because you cannot spread threads across multiple and different nodes

### Use case: Multithreaded Servers

Workflow of a server:

 1. Opens a socket

 2. Listens for requests

 3. Process request

If you run your server on a single thread, then we can only process a new request after the current one is completed.

Hence, we can implement multithreaded servers as a solution!

    - Start new thread for each request made

    - However, creates a lot of overhead, hence use thread pool/tasks so that you can reuse threads rather than creating new ones

### Use case: Adding multithreading to MPI

Most nodes/data centers have multiple processor cores executing on them

But when we run an MPI interface on a node, we are only running it on one thread on of one of the processors

We can add multithreading!

- Start of with Master Thread, which initializes MPI and starts to create workers

- Everytime we make a call to MPI API and are awaiting further instructions, other workers can continue to carry out/wait for other requests

- Finishes by calling `.FINALIZE()`

- Threading Modes in MPI:

    1. Funneled (all MPI calls by one thread)
    
    2. Serialized (1 MPI call at a time)

    3. Multiple (multiple MPI calls at the same time. Some cavaets when using this mode, such as you cannot have 2 threads waiting on the same events)

### Distributed Actors

Actors are objects linked to concurrency, consisting of Mailbox, Local State and Methods

Actors are used to solve problems such as Sieve of Eratosthenes

However, we may run into issues if we have too many actors executing on a single node. Hence, distribute them! Procedure:

 1. Create configuration file

 2. Create remote actors

 3. Look up remote actors by logical name

 4. Message serialisation

### Reactive Programming

Implementation of distributed service oriented architectures (micro services) with asynchronicity

Model of Reactive Programming:

1. Publisher

2. Subscriber

However, need to make it more efficient as...

- If we have actors that are just pushing information as publishers, subscribers could be overwhelmed

- If we have actors that are just pulling information as subscribers, there will be alot of long delays and it would be more efficient for publishers to push information in batches

- It is very important to implement these microservice architectures efficiently. And to do so, you need an API that can combine both the push and the pull of the models

Reactive Programming aims to systemize this, via reactive streams specification.

- Components:

    1. Publisher

    2. Subscriber

    3. Process

    4. Subscription

- Methods (which allows for control over the batching of information between the publisher and the subscriber by implementing this logic as to when to issue a request and how many items to request)

    1. `.OnSubscribe()`

    2. `.OnNext()`



