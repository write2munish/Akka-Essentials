package org.akka.essentials.scala.future.example
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import scala.concurrent.Future
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ProcessOrderActor extends Actor {

  implicit val timeout = Timeout(5 seconds)
  val orderActor = context.actorOf(Props[OrderActor])
  val addressActor = context.actorOf(Props[AddressActor])
  val orderAggregateActor = context.actorOf(Props[OrderAggregateActor])

  def receive = {
    case userId: Integer =>
      val aggResult: Future[OrderHistory] =
        for {
          order <- ask(orderActor, userId).mapTo[Order] // call pattern directly
          address <- addressActor ask userId mapTo manifest[Address] // call by implicit conversion
        } yield OrderHistory(order, address)
      aggResult pipeTo orderAggregateActor
  }
}