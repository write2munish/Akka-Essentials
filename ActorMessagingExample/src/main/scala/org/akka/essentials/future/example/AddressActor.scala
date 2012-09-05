package org.akka.essentials.future.example
import akka.actor.Actor

class AddressActor extends Actor {

  def receive = {
    case userId: Int =>
      sender ! new Address(userId, "Munish Gupta", "Sarjapura Road", "Bangalore, India")
  }
}