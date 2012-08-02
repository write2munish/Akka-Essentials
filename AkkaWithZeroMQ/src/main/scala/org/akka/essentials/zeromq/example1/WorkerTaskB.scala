package org.akka.essentials.zeromq.example1
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.zeromq.Connect
import akka.zeromq.Listener
import akka.zeromq.SocketType
import akka.zeromq.Subscribe
import akka.zeromq.ZMQMessage
import akka.zeromq.ZeroMQExtension

class WorkerTaskB extends Actor with ActorLogging {
  val subSocket = ZeroMQExtension(context.system).newSocket(SocketType.Sub, Connect("tcp://127.0.0.1:1234"), Listener(self), Subscribe("someTopic"))

  def receive = {
    case m: ZMQMessage =>
      var mesg = new String(m.payload(1))
      log.info("Received Message @ B -> {}", mesg)
  }
}