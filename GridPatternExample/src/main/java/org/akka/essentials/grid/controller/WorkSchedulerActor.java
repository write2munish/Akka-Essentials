package org.akka.essentials.grid.controller;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkSchedulerActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Integer) {
			getContext()
					.system()
					.scheduler()
					.scheduleOnce(
							Duration.create(((Integer) message).intValue(),
									TimeUnit.SECONDS), getContext().sender(),
							"SendWork",getContext().dispatcher());
		} else if (message instanceof String) {
			log.info("Recieved work sending request");
			getContext()
					.system()
					.scheduler()
					.scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),
							getContext().sender(), "SendWork",getContext().dispatcher());
		}
	}

}
