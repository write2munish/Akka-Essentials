package org.akka.essentials.zeromq.example4

import akka.actor.ActorSystem
import akka.actor.Props

object MyActorSystem {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("zeromqTest")
    system.actorOf(Props[PushActor], name = "push")
    system.actorOf(Props[PullActor1], name = "pull1")
    system.actorOf(Props[PullActor2], name = "pull2")
  }
}