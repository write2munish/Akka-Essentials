package org.akka.essentials.grid.worker;

import org.akka.essentials.grid.Task;
import org.akka.essentials.grid.TaskFinished;

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
			}
		} else if (message instanceof Task) {
			Task task = (Task) message;
			// check whether we have processed enough messages
			if (messageProcessedCounter == 100) {
				// tell the server, enough messages for me and shut me down
				getContext().stop(getSelf());
				getContext().system().shutdown();
			} else {
				log.info("Work Packet from server->" + task.getTaskNumber());
				messageProcessedCounter++;
				getSender().tell(new TaskFinished(task.getTaskNumber()));
			}
		}
	}

	@Override
	public void postStop() {
		log.info("Stopping WorkerActor instance #" + instanceCounter
				+ ", hashcode #" + this.hashCode());
		instanceCounter--;
	}

}
