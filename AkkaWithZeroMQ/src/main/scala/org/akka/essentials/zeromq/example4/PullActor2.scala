package org.akka.essentials.zeromq.example4
import akka.zeromq.ZeroMQExtension
import akka.actor.ActorLogging
import akka.zeromq.ZMQMessage
import akka.actor.Actor
import akka.zeromq.SocketType
import akka.zeromq.Connect
import akka.zeromq.Listener

class PullActor2 extends Actor with ActorLogging {
  val pullSocket = ZeroMQExtension(context.system).newSocket(SocketType.Pull, Connect("tcp://127.0.0.1:1234"), Listener(self))

  def receive: Receive = {
    case m: ZMQMessage =>
      var mesg = new String(m.payload(0))
      log.info("Received Message -> {}", mesg)
  }
}