package org.akka.essentials.clientserver.example
import akka.actor.Actor

class ServerActor extends Actor {
  def receive: Receive = {
    case message: String =>
      // Get reference to the message sender and reply back
      sender.tell(message + " got something")
  }
}