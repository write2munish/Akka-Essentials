package org.akka.essentials.unittest.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BoomActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String)
			throw new IllegalArgumentException("boom!");
		else if (message instanceof Integer)
			throw new NullPointerException("caput");
		else
			unhandled(message);
	}
}