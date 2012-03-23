package org.akka.essentials.grid.worker;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkerActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private static int instanceCounter = 0;
	private static int messageProcessedCounter = 0;

	@Override
	public void preStart() {
		instanceCounter++;
		log.info("Starting WorkerActor instance #" + instanceCounter
				+ ", hashcode #" + this.hashCode());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			if (((String) message).compareTo("STOP") == 0) {
				// used when the new routee gets added to the server
				// we stop the currently running actor's on each client
				getContext().stop(getSelf());
			} else {
				log.info("Work Packet from server->" + message);

				messageProcessedCounter++;

				log.info("Sender Path" + getSender().path().toString());
				
				// check whether we have processed enough messages
				if (messageProcessedCounter == 20) {
					// tell the server, enough messages for me and shut me down
					getSender().tell("Enough!",this.getSelf());
				}
			}
		}

	}

	@Override
	public void postStop() {
		log.info("Stoping WorkerActor instance #" + instanceCounter
				+ ", hashcode #" + this.hashCode());
		instanceCounter--;
	}

}
