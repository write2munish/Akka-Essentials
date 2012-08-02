package org.akka.essentials.zeromq.example3.server
import akka.actor.ActorSystem
import akka.actor.Props

object MyServerActorSystem {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("zeromqServerTest")
    system.actorOf(Props[ServerActor], name = "server")
  }
}