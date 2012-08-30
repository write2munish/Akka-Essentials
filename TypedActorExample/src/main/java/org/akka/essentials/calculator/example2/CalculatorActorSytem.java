package org.akka.essentials.calculator.example2;

import java.util.Arrays;

import org.akka.essentials.calculator.Calculator;
import org.akka.essentials.calculator.CalculatorInt;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.routing.BroadcastRouter;

public class CalculatorActorSytem {

	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("TypedActorsExample");

		CalculatorInt calculator1 = TypedActor.get(_system).typedActorOf(
				new TypedProps<Calculator>(CalculatorInt.class,
						Calculator.class));

		CalculatorInt calculator2 = TypedActor.get(_system).typedActorOf(
				new TypedProps<Calculator>(CalculatorInt.class,
						Calculator.class));

		// Create a router with Typed Actors
		ActorRef actor1 = TypedActor.get(_system).getActorRefFor(calculator1);
		ActorRef actor2 = TypedActor.get(_system).getActorRefFor(calculator2);

		Iterable<ActorRef> routees = Arrays.asList(new ActorRef[] { actor1,
				actor2 });
		ActorRef router = _system.actorOf(new Props()
				.withRouter(BroadcastRouter.create(routees)));

		router.tell("Hello there");

		_system.shutdown();

	}

}
