package org.akka.essentials.router.custom.example
import akka.actor.Actor
import akka.actor.ActorLogging

class MsgEchoActor extends Actor with ActorLogging{
  def receive: Receive = {
    case message =>
      log.info("Received Message {} in Actor {}", message, self.path.name)
  }
}