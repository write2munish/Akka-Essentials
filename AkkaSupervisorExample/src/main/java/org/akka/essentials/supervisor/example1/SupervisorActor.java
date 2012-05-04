package org.akka.essentials.supervisor.example1;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import org.akka.essentials.supervisor.example1.MyActorSystem.Result;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;
import akka.util.Duration;

public class SupervisorActor extends UntypedActor {

	public ActorRef childActor;

	public SupervisorActor() {
		childActor = getContext().actorOf(new Props(WorkerActor.class),
				"workerActor");
	}

	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
			Duration.parse("10 second"), new Function<Throwable, Directive>() {
				public Directive apply(Throwable t) {
					if (t instanceof ArithmeticException) {
						return resume();
					} else if (t instanceof NullPointerException) {
						return restart();
					} else if (t instanceof IllegalArgumentException) {
						return stop();
					} else {
						return escalate();
					}
				}
			});

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	public void onReceive(Object o) throws Exception {
		if (o instanceof Result) {
			childActor.tell(o, getSender());
		} else
			childActor.tell(o);
	}
}
