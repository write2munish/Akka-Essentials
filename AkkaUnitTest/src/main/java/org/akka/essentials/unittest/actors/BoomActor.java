package org.akka.essentials.unittest.actors;

import akka.actor.UntypedActor;

public class BoomActor extends UntypedActor {
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String)
			throw new IllegalArgumentException("boom!");
		else if (message instanceof Integer)
			throw new NullPointerException("caput");
	}
}