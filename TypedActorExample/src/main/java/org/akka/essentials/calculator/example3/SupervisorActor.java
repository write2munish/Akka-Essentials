package org.akka.essentials.calculator.example3;

import static akka.actor.SupervisorStrategy.restart;

import org.akka.essentials.calculator.CalculatorInt;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.TypedActor;
import akka.actor.TypedActor.PostStop;
import akka.actor.TypedActor.PreStart;
import akka.actor.TypedActor.Receiver;
import akka.actor.TypedActor.Supervisor;
import akka.dispatch.Future;
import akka.dispatch.Futures;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import akka.japi.Option;
import akka.util.Duration;

public class SupervisorActor implements Receiver, CalculatorInt, PreStart,
		PostStop, Supervisor {

	LoggingAdapter log = Logging.getLogger(TypedActor.context().system(), this);
	Integer counter = 0;
	
	//create a child actor under the Typed Actor context
	ActorRef childActor = TypedActor.context().actorOf(
			new Props(ChildActor.class), "childActor");

	/**
	 * Non blocking request response
	 */
	public Future<Integer> add(Integer first, Integer second) {
		return Futures.successful(first + second, TypedActor.dispatcher());
	}

	/**
	 * Non blocking request response
	 */
	public Future<Integer> subtract(Integer first, Integer second) {
		return Futures.successful(first - second, TypedActor.dispatcher());

	}

	/**
	 * fire and forget
	 */
	public void incrementCount() {
		counter++;
	}

	/**
	 * Blocking request response
	 */
	public Option<Integer> incrementAndReturn() {
		return Option.some(++counter);
	}

	/**
	 * Allows to tap into the Actor PreStart hook
	 */
	public void preStart() {
		log.info("Actor Started !");
	}

	public void onReceive(Object msg, ActorRef actor) {
		log.info("Received Message -> " + msg);
		childActor.tell(msg,actor);
	}

	/**
	 * Allows to tap into the Actor PostStop hook
	 */
	public void postStop() {
		log.info("Actor Stopped ! ");
	}

	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	private static SupervisorStrategy strategy = new OneForOneStrategy(-1,
			Duration.Inf(), new Function<Throwable, Directive>() {
				public Directive apply(Throwable t) {
					return restart();
				}
			});

}
