package org.akka.essentials.unittest.actors;

import akka.actor.UntypedActor;

public class EchoActor extends UntypedActor {
	@Override
	public void onReceive(Object message) throws Exception {
		sender().tell(message);
	}
}
