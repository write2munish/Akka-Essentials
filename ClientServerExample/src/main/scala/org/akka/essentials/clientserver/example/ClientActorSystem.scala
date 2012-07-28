package org.akka.essentials.clientserver.example

import com.typesafe.config.ConfigFactory

import akka.actor.actorRef2Scala
import akka.actor.ActorSystem
import akka.actor.Props

object ClientActorSystem {
  def main(args: Array[String]): Unit = {
    // load the configuration
    val config = ConfigFactory.load().getConfig("ClientSys")

    val system = ActorSystem("WorkerSys", config)

    val clientActor = system.actorOf(Props[ClientActor], name = "clientActor")

    clientActor ! "Hi there"

    Thread.sleep(4000)

    system.shutdown()
  }
}