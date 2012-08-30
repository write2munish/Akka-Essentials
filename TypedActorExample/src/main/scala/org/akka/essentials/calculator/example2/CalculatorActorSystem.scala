package org.akka.essentials.calculator.example2
import org.akka.essentials.calculator.Calculator
import org.akka.essentials.calculator.CalculatorInt
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.TypedActor
import akka.routing.BroadcastRouter
import akka.actor.TypedProps

object CalculatorActorSystem {
  def main(args: Array[String]): Unit = {

    val _system = ActorSystem("TypedActorsExample")

    val calculator1: CalculatorInt =
      TypedActor(_system).typedActorOf(TypedProps[Calculator]())

    val calculator2: CalculatorInt =
      TypedActor(_system).typedActorOf(TypedProps[Calculator]())

    // Create a router with Typed Actors
    val actor1: ActorRef = TypedActor(_system).getActorRefFor(calculator1)
    val actor2: ActorRef = TypedActor(_system).getActorRefFor(calculator2)

    val routees = Vector[ActorRef](actor1, actor2)
    val router = _system.actorOf(new Props().withRouter(
      BroadcastRouter(routees = routees)))

    router.tell("Hello there")

    _system.shutdown()
  }
}