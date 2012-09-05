package org.akka.essentials.hotswap.example
import akka.actor.ActorSystem
import akka.actor.Props

object MyActorSystem {

  def main(args: Array[String]): Unit = {
    val _system = ActorSystem("BecomeUnbecome")
    val pingPongActor = _system.actorOf(Props[PingPongActor])
    pingPongActor ! PING
    Thread.sleep(2000)
    _system.shutdown
  }
}