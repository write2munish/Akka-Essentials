package org.akka.essentials.unittest.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class ForwardingActor extends UntypedActor {
	ActorRef next;
	public ForwardingActor(ActorRef next) {
		this.next = next;
	}
	@Override
	public void onReceive(Object message) throws Exception {
		next.tell(message);
	}
}
