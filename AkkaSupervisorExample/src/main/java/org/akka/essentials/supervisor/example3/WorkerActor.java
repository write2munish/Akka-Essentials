package org.akka.essentials.supervisor.example3;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkerActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	@SuppressWarnings("unused")
	private int state = 0;

	@Override
	public void preStart() {
		log.info("Starting WorkerActor instance hashcode #" + this.hashCode());
	}

	public void onReceive(Object o) throws Exception {
		if (o instanceof Integer) {
			Integer value = (Integer) o;
			state = value;
			log.info("Received a message " + value);
		} else if (o instanceof Result) {
			getSender().tell(state);
		} else {
			throw new IllegalArgumentException("Wrong Arguement");
		}
	}

	@Override
	public void postStop() {
		log.info("Stopping WorkerActor instance hashcode #" + this.hashCode());

	}
}
