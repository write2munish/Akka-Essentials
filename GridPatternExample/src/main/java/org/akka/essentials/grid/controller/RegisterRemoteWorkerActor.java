package org.akka.essentials.grid.controller;

import org.akka.essentials.grid.StartWorker;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class RegisterRemoteWorkerActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef jobControllerActor;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof StartWorker) {
			log.info("Recieved Registration Info from " + message.toString());
			jobControllerActor.tell(message);
		} else
			log.error("Wrong message type recieved");
	}

	public RegisterRemoteWorkerActor(ActorRef inJobControllerActor) {
		jobControllerActor = inJobControllerActor;
	}

}
