package org.akka.essentials.unittest.example
import akka.actor.Actor

class EchoActor extends Actor {

  def receive: Receive = {
    case message => sender ! message
  }
}