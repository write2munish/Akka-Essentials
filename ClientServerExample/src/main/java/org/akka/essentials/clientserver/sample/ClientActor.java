package org.akka.essentials.clientserver.sample;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClientActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef remote;

	public ClientActor(ActorRef inActor) {
		remote = inActor;
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof String) {
			if (((String) message).startsWith("Start") == true) {
				log.info("Sending message to server - message# Hi there");
				remote.tell("Hi there", getSelf());
			} else {
				log.info("Message received from Server -> " + message);
			}
		}

	}

}
