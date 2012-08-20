package org.akka.essentials.unittest.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class FilteringActor extends UntypedActor {
	ActorRef next;

	public FilteringActor(ActorRef next) {
		this.next = next;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String)
			next.tell(message);
	}
}
