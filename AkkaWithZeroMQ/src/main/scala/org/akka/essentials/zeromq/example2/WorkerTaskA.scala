package org.akka.essentials.zeromq.example2
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.zeromq.Connect
import akka.zeromq.Identity
import akka.zeromq.Listener
import akka.zeromq.SocketType
import akka.zeromq.ZMQMessage
import akka.zeromq.ZeroMQExtension
import akka.zeromq.Frame

class WorkerTaskA extends Actor with ActorLogging {
  val subSocket = ZeroMQExtension(context.system).newSocket(SocketType.Dealer, Connect("tcp://127.0.0.1:1234"), Listener(self), Identity("A".getBytes()))

  def receive = {
    case m: ZMQMessage =>
      var mesg = new String(m.payload(0))
      subSocket.tell((new ZMQMessage(Frame(mesg
        + " - Workload Processed by A"))))
  }
}