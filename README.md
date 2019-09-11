Akka Essentials
(refer http://akka-essentials.blogspot.com/ for more details)

[![Build Status](https://api.travis-ci.org/write2munish/Akka-Essentials.png)](https://api.travis-ci.org/write2munish/Akka-Essentials)

You will find examples of Akka in Java & Scala, talking of one concept along with a problem solved

ClientServerExample : This example demonstrates how the remote actors works in a client / server mode. The client sends the message to the server and server replies back to the client. The example also demonstrates various methods of creating remote actor references on the client side.

LoadGeneratorExample : This example generates 10 million messages and calculates the time it takes to process them. The program demonstrated the Routing concept where a roundrobinrouter is used to distribute the load on to a set of workers

WordCountMapReduce : This examples implements the Word Count Map Reduce model. The client system reads a text file and sends each line of text as a message to the Server. The server reads the line, maps the words, reduces the words and finally does an inmemory aggregation of the result. The example also implemented a prioritymailbox, which is used to segregate the message requests between the mapreduce requests and getting the list of results from the aggregate actor

GridPatternExample : Grid Computing pattern is where a control node distributes the work to other nodes. Idea is to make use of the nodes on the network for their computing power. It is analogous to Master Slave Pattern with certain differences. The idea behind the Master Slave pattern is to partition the work into identical sub tasks which are then delegated to Slaves. The example demonstrates how an WorkerActor system sends a request for registration. The RegisterRemoteWorker recieves the request and forwards the same to JobController where the RoundRobinRouter is updated for the new worker information. The WorkScheduler sends a periodic request to JobController, who then sends packets to all the registered worker actors.

AkkaSerializableExample : Akka by default supports 2 serializer options - java and protobuf. In addition, Akka provides an API to write you owns serializable. In this example, i have used google gson library to convert your value object into json string representation which is then converted to bytes and transported across the wire

AkkaSupervisorExample : Akka provides two supervisor strategies - One-For-One or All-For-One that are used to monitor the actors and build the fault tolerance in the actor model. There are 3 examples that demonstrate the strategies and their usage. In addition, the java section has unit testing code also for testing your supervisor code

AkkaWithZeroMQ - Akka provides native support for ZeroMQ libraries and provide different connectors (Pub-Sub, Req-Rep,Router-Dealer and Pull-Push). There are four examples that demonstrated their usage

AkkaUnitTest : Unit testing toolkit is provided via TestKit in Akka. The scala side of unit testing is well covered. For java, TestKit provides limited constructs. The various examples implemented by Ray Roestenburg have ported to Java world, with couple of more scenario's added. This can be good starting point for Java programmers to start unit testing their actors

AkkaPersistentExample : Akka provides persistent model for stateful actors. The example uses a simple example of Integer (which carries the ) and operations (ADD, SUBTRACT, MULTIPLY, DIVIDE) along with operand acts on the state object
