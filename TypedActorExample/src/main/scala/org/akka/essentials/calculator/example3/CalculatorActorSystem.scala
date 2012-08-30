package org.akka.essentials.calculator.example3
import org.akka.essentials.calculator.CalculatorInt

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.TypedActor
import akka.actor.TypedProps
import akka.dispatch.Await
import akka.pattern.ask
import akka.util.Timeout
import akka.util.duration._

object CalculatorActorSystem {
  def main(args: Array[String]): Unit = {

    val _system = ActorSystem("TypedActorsExample")

    val calculator: CalculatorInt =
      TypedActor(_system).typedActorOf(TypedProps[SupervisorActor]())

    // Get access to the ActorRef
    val calActor: ActorRef = TypedActor(_system).getActorRefFor(calculator)
    // call actor with a message
    calActor.tell("Hi there", calActor)

    //wait for child actor to get restarted
    Thread.sleep(500)

    // Invoke the method and wait for result
    implicit val timeout = Timeout(5 seconds)
    val future = (calActor ? 10).mapTo[Integer] // enabled by the “ask” import
    val result = Await.result(future, timeout.duration)

    println("Result from child actor->" + result)

    //wait before shutting down the system
    Thread.sleep(500)

    _system.shutdown()
  }
}