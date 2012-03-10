package org.akka.essentials.loadgenerator;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.util.Duration;

public class WorkerActor extends UntypedActor {

	private ActorRef jobController;

	@Override
	public void onReceive(Object message) throws Exception {
		// using scheduler to send the reply after 1000 milliseconds
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),
						jobController, "Done");
	}

	public WorkerActor(ActorRef inJobController) {
		jobController = inJobController;
	}
}
