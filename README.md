# AsynchBellmanFord

The problem :
Implement the Asynchronous Bellman-Ford algorithm. Compute the total number of messages sent for
the run and output the result.
The message transmission time for each link for each message is to be randomly chosen using a uniform distribution
in the range 1 to 15 “time units.” All links are bidirectional and FIFO. (FIFO: If I send two messages
m1 and then m2 to you, then you receive m1 first and then m2.)
Extend the Asynchronous Bellman-Ford algorithm to have a simple synchronizer: In every “round,” if
a process has no message to send to a neighbor, it will send a “dummy” message to a neighbor. With
this modification, compute the total number of messages sent, including the “dummy” messages and
output the result.
# Input
The program will read in the following information in this order from an input file called connectivity.txt:
The first line has a single integer and it represents the total number of processes in the system. The ids are 0, 1,
2,…n-1 where n is the total number of processes in the system.
The second line indicates the id of the root of the shortest paths tree to be built.
Lines 3 to n+2 represent the weight matrix (floating point numbers represented in decimal form) with each line
representing the weights of all links connected to a single process. Thus, line i+3 has n numbers representing the
weight of the edges incident on the ith process. The jth component of line i+3 represents the edge weight of link
(i,j).
n
id of root (which is a number in the range 0..n-1)
weight matrix (which is symmetric).
A weight of -1 signifies no link.
The weights are non-negative, except for -1 for no link.

# Language Used
- Java version "1.8.0_171"
- Multithreading library for spawning threads
