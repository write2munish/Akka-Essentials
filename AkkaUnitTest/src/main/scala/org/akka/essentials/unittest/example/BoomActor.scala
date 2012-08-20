package org.akka.essentials.unittest.example
import akka.actor.Actor

class BoomActor extends Actor {
  def receive: Receive = {
    case message: String => throw new IllegalArgumentException("boom!")
    case message: Integer => throw new NullPointerException("caput")
  }
}