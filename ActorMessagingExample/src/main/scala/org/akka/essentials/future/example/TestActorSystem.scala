package org.akka.essentials.future.example
import akka.actor.ActorSystem
import akka.actor.Props

case class Order(userId: Int, orderNo: Int, amount: Float, noOfItems: Int)
case class Address(userId: Int, fullName: String, address1: String, address2: String)
case class OrderHistory(order: Order, address: Address)

object TestActorSystem {

  def main(args: Array[String]): Unit = {

    val _system = ActorSystem("FutureUsageExample")
    val processOrder = _system.actorOf(Props[ProcessOrderActor])
    processOrder ! 456
    Thread.sleep(5000)
    _system.shutdown
  }
}