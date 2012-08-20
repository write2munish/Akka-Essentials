package org.akka.essentials.unittest.example
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef

class FilteringActor(next: ActorRef) extends Actor {
  def receive: Receive = {
    case message: String => next ! message
  }
}