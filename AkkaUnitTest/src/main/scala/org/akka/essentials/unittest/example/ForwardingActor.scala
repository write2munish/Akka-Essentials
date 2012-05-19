package org.akka.essentials.unittest.example
import akka.actor.ActorRef
import akka.actor.Actor

class ForwardingActor(next: ActorRef) extends Actor {

  def receive: Receive = {
    case message => next ! message
  }
}