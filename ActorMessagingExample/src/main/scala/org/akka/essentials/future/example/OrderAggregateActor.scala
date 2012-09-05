package org.akka.essentials.future.example
import akka.actor.ActorLogging
import akka.actor.Actor

class OrderAggregateActor extends Actor {
  def receive = {
    case orderHistory: OrderHistory =>
      println(orderHistory.toString())
  }
}