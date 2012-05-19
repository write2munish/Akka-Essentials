package org.akka.essentials.unittest.example
import akka.actor.Actor
import akka.actor.ActorRef

class SequencingActor(next: ActorRef, head: List[Integer], tail: List[Integer]) extends Actor {

  def receive: Receive = {
    case message =>
      head map (next ! _)
      next ! message
      tail map (next ! _)
  }
}