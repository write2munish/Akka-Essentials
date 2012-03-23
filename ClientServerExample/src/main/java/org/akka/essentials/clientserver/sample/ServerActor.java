package org.akka.essentials.clientserver.sample;

import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ServerActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private static int instanceCounter = 0;

	@Override
	public void preStart() {
		instanceCounter++;
		log.info("Starting ServerActor instance #" + instanceCounter
				+ ", hashcode #" + this.hashCode());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			getSender().tell(message + " got something");
		} else if (message instanceof PoisonPill) {
			getContext().system().shutdown();
		}
	}

	@Override
	public void postStop() {
		log.info("Stoping ServerActor instance #" + instanceCounter
				+ ", hashcode #" + this.hashCode());
		instanceCounter--;
	}

}
