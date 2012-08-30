package org.akka.essentials.calculator.example3
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.event.Logging
import akka.actor.ActorLogging

class ChildActor extends Actor with ActorLogging {

  override def preStart() {
    log.info("Child Actor Started > {}", self.path)
  }

  def receive: Receive = {
    case message: String => throw new IllegalArgumentException("boom!")
    case message: Integer => sender ! message * message
    case _ => unhandled()
  }

  override def postStop() {
    log.info("Child Actor Stopped > {}", self.path)
  }
}