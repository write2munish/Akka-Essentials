package org.akka.essentials.durable.mailbox.example
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import com.typesafe.config.ConfigFactory

object DurableMailBox {

  class DurableMailBox extends Actor {
    def receive: Receive = {
      case _ =>
    }
  }
  
  def main(args: Array[String]): Unit = {}
  
  val system = ActorSystem("DurableMailBox", ConfigFactory.load()
    .getConfig("myapp1"))
    
  val actor = system.actorOf(Props[DurableMailBox].withDispatcher("my-dispatcher"), name = "serverActor")

  actor ! "Hello"
}