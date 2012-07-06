package org.akka.essentials.hotswap;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;

public class CalculateActorSystem {

	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("CalculateSystem");
		ActorRef myActor = _system.actorOf(new Props(CalculatorActor.class),
				"myCalciActor");

		Timeout timeout = new Timeout(Duration.parse("5 seconds"));
		Future<Object> future = Patterns.ask(myActor, new Numbers(14, 6),
				timeout);
		Integer result = (Integer) Await.result(future, timeout.duration());
		System.out.println("Result is " + result);

		myActor.tell(CalculatorActor.SUBTRACT);

		future = Patterns.ask(myActor, new Numbers(14, 6), timeout);
		result = (Integer) Await.result(future, timeout.duration());
		System.out.println("Result is " + result);

		myActor.tell(CalculatorActor.MULTIPLY);

		future = Patterns.ask(myActor, new Numbers(14, 6), timeout);
		result = (Integer) Await.result(future, timeout.duration());
		System.out.println("Result is " + result);
		
		myActor.tell(CalculatorActor.DIVIDE);

		future = Patterns.ask(myActor, new Numbers(12, 6), timeout);
		result = (Integer) Await.result(future, timeout.duration());
		System.out.println("Result is " + result);
		
		_system.shutdown();
	}

}
