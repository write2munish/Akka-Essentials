package org.akka.essentials.grid.controller;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class RegisterRemoteWorkerActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef jobControllerActor;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Address) {
			Address add = (Address) message;
			log.info("Recieved Registration Info from " + add.toString());
			jobControllerActor.tell(message);
		} else
			log.error("Wrong message type recieved");
	}

	public RegisterRemoteWorkerActor(ActorRef inJobControllerActor) {
		jobControllerActor = inJobControllerActor;
	}

}
