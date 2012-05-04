package org.akka.essentials.supervisor.example3;

import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MonitorActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	Map<ActorRef, ActorRef> monitoredActors = new HashMap<ActorRef, ActorRef>();

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Terminated) {
			final Terminated t = (Terminated) message;
			if (monitoredActors.containsKey(t.getActor())) {
				log.info("Received Worker Actor Termination Message -> "
						+ t.getActor().path());
				log.info("Sending message to Supervisor");
				monitoredActors.get(t.getActor()).tell(new DeadWorker());
			}
		} else if (message instanceof RegisterWorker) {

			RegisterWorker msg = (RegisterWorker) message;
			getContext().watch(msg.worker);
			monitoredActors.put(msg.worker, msg.supervisor);

		} else {
			unhandled(message);
		}
	}
}
