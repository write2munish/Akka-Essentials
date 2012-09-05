package org.akka.essentials.future.example
import akka.actor.Actor

class OrderActor extends Actor {

  def receive = {
    case userId: Int =>
      sender ! new Order(userId, 123, 345, 5)
  }
}