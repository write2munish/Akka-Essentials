package org.akka.essentials.localnode
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props

object LocalNodeApplication {

  def main(args: Array[String]): Unit = {
    // load the configuration
    val config = ConfigFactory.load().getConfig("LocalSys")
    val system = ActorSystem("LocalNodeApp", config)
    val clientActor = system.actorOf(Props[LocalActor])
    clientActor ! "Hello"
    Thread.sleep(4000)
    system.shutdown()
  }
}