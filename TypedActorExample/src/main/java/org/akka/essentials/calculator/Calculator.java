package org.akka.essentials.calculator;

import akka.actor.ActorRef;
import akka.actor.TypedActor;
import akka.actor.TypedActor.PostStop;
import akka.actor.TypedActor.PreStart;
import akka.actor.TypedActor.Receiver;
import akka.dispatch.Future;
import akka.dispatch.Futures;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Option;

public class Calculator implements Receiver, CalculatorInt, PreStart, PostStop {

	LoggingAdapter log = Logging.getLogger(TypedActor.context().system(), this);
	Integer counter = 0;

	// Non blocking request response
	public Future<Integer> add(Integer first, Integer second) {
		return Futures.successful(first + second, TypedActor.dispatcher());
	}

	// Non blocking request response
	public Future<Integer> subtract(Integer first, Integer second) {
		return Futures.successful(first - second, TypedActor.dispatcher());

	}

	// fire and forget
	public void incrementCount() {
		counter++;
	}

	// Blocking request response
	public Option<Integer> incrementAndReturn() {
		return Option.some(++counter);
	}

	// Allows to tap into the Actor PreStart hook
	public void preStart() {
		log.info("Actor Started ! ");
	}

	public void onReceive(Object msg, ActorRef actor) {
		log.info("Received Message -> {}", msg);
	}

	// Allows to tap into the Actor PostStop hook
	public void postStop() {
		log.info("Actor Stopped ! ");
	}
}
