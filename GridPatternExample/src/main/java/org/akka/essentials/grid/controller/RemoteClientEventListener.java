package org.akka.essentials.grid.controller;

import org.akka.essentials.grid.StopWorker;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.remote.RemoteClientError;

public class RemoteClientEventListener extends UntypedActor {
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef jobScheduler;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof RemoteClientError) {
			RemoteClientError event = (RemoteClientError) message;
			log.info("Received remote client error event from address "
					+ event.getRemoteAddress());
			jobScheduler.tell(new StopWorker(event.getRemoteAddress()
					.toString()));
			log.info("Cause of the event was {}", event.getCause());
//		} else if (message instanceof RemoteClientWriteFailed) {
//			RemoteClientWriteFailed event = (RemoteClientWriteFailed) message;
//			log.info("Received remote client write fail event from address "
//					+ event.getRemoteAddress());
//			jobScheduler.tell(new StopWorker(event.getRemoteAddress()
//					.toString()));
		}
	}

	public RemoteClientEventListener(ActorRef inJobScheduler) {
		jobScheduler = inJobScheduler;
	}
}
