package org.akka.essentials.hotswap
import akka.actor.ActorSystem
import akka.actor.Props
import akka.dispatch.Await
import akka.util.Timeout
import akka.util.duration._
import akka.pattern.ask

object CalculateActorSystem {

	def main(args: Array[String]): Unit = {
		val _system = ActorSystem("CalculateSystem")
		val myActor = _system.actorOf(Props[CalculatorActor], name = "myCalciActor")

		implicit val timeout = Timeout(5 seconds)
		var future = myActor ? Numbers(14, 6)
		var result = Await.result(future, timeout.duration).asInstanceOf[Integer]
		System.out.println("Result is " + result)

		myActor ! SUBTRACT
		
		future = myActor ? Numbers(14, 6)
		result = Await.result(future, timeout.duration).asInstanceOf[Integer]
		System.out.println("Result is " + result)

		myActor ! MULTIPLY

		future = myActor ? Numbers(14, 6)
		result = Await.result(future, timeout.duration).asInstanceOf[Integer]
		System.out.println("Result is " + result)

		myActor ! DIVIDE

		future = myActor ? Numbers(12, 6)
		result = Await.result(future, timeout.duration).asInstanceOf[Integer]
		System.out.println("Result is " + result)

		_system.shutdown()
	}
}