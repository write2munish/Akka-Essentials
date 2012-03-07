package org.akka.essentials.clientserver.sample;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class ClientActor extends UntypedActor {

	private ActorRef remote;

	public ClientActor(ActorRef inActor) {
		remote = inActor;
	}

	@Override
	public void onReceive(Object arg0) throws Exception {

		if (arg0 instanceof String) {
			if (((String) arg0).compareTo("Start") == 0) {
				remote.tell("Hi there", getSelf());
			} else {
				System.out.println(arg0);
				getContext().system().shutdown();
			}
		}

	}

}
