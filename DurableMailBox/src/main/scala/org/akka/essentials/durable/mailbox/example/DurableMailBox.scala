package org.akka.essentials.durable.mailbox.example
import com.typesafe.config.ConfigFactory

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

object DurableMailBox {

  class DurableMailBox extends Actor {
    def receive: Receive = {
      case message:String => System.out.println("Received Message " + message)
    }
  }

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("DurableMailBox", ConfigFactory.load()
      .getConfig("myapp1"))

    val actor = system.actorOf(Props[DurableMailBox].withDispatcher("my-dispatcher"), name = "serverActor")

    actor ! "Hello"
    
    Thread.sleep(500)
    
    system.shutdown
  }
}