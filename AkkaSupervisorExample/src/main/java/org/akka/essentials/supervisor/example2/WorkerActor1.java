package org.akka.essentials.supervisor.example2;

import org.akka.essentials.supervisor.example2.MyActorSystem2.Result;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkerActor1 extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private int state = 0;

	@Override
	public void preStart() {
		log.info("Starting WorkerActor1 instance hashcode #" + this.hashCode());
	}

	public void onReceive(Object o) throws Exception {
		if (o == null) {
			throw new NullPointerException("Null Value Passed");
		} else if (o instanceof Integer) {
			Integer value = (Integer) o;
			if (value <= 0) {
				throw new ArithmeticException("Number equal or less than zero");
			} else
				state = value;
		} else if (o instanceof Result) {
			getSender().tell(state);
		} else {
			throw new IllegalArgumentException("Wrong Arguement");
		}
	}

	@Override
	public void postStop() {
		log.info("Stopping WorkerActor1 instance hashcode #" + this.hashCode());

	}
}
