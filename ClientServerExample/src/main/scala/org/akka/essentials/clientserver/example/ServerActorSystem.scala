package org.akka.essentials.clientserver.example

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props

object ServerActorSystem {
  def main(args: Array[String]): Unit = {
    // load the configuration
    val config = ConfigFactory.load().getConfig("ServerSys")

    val system = ActorSystem("ServerSys", config)

    val serverActor = system.actorOf(Props[ServerActor], name = "serverActor")
  }

}