package org.akka.essentials.clientserver.sample;

import akka.actor.UntypedActor;

public class ServerActor extends UntypedActor {

	@Override
	public void onReceive(Object arg0) throws Exception {
		if(arg0 instanceof String){
			getSender().tell(arg0 + " got something");
		}
		
	}

}
