package org.akka.essentials.scala.router.example
import akka.actor.Actor
import akka.actor.ActorLogging

class MsgEchoActor extends Actor with ActorLogging {
  def receive: Receive = {
    case message =>
      Thread.sleep(10)
      log.info("Received Message {} in Actor {}", message, self.path.name)
  }
}
