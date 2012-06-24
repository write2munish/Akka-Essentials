package org.akka.essentials.dispatcher;

import akka.actor.UntypedActor;

public class MsgEchoActor extends UntypedActor {

	int messageProcessed = 0;
	
	@Override
	public void onReceive(Object msg) throws Exception {
		messageProcessed++;
		
		System.out.println(String.format(
				"Received Message '%s' in Actor %s using Thread %s, total message processed %s", msg,
				getSelf().path().name(), Thread.currentThread().getName(),messageProcessed));
	}

}
