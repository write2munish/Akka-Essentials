package org.akka.essentials.calculator.example3;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ChildActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void preStart() {
		log.info("Child Actor Started > {}", self().path());
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof String) {
			throw new IllegalArgumentException("boom!");
		} else if (message instanceof Integer) {
			Integer value = (Integer) message;
			getSender().tell(value * value);
		} else
			unhandled(message);

	}

	public void postStop() {
		log.info("Child Actor Stopped > {}", self().path());
	}

}
