package org.akka.essentials.unittest.actors;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;


public class SupervisorActor extends UntypedActor {

	private ActorRef childActor;

	public ActorRef getChildActor() {
		return childActor;
	}

	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
			Duration.create("10 second"), new Function<Throwable, Directive>() {
				public Directive apply(Throwable t) {
					if (t instanceof IllegalArgumentException) {
						return stop();
					} else if (t instanceof NullPointerException) {
						return resume();
					} else
						return escalate();
				}
			});

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	public void onReceive(Object o) throws Exception {
		if (o instanceof Props) {
			this.childActor = getContext().actorOf((Props) o, "childActor");
			sender().tell(childActor);
		} else
			childActor.tell(o, sender());
	}
}
