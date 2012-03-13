package org.akka.essentials.wc.mapreduce.example.client;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class ClientActor extends UntypedActor {

	private ActorRef remoteServer = null;
	@SuppressWarnings("unused")
	private ActorRef fileReadActor = null;
	private long start;

	/**
	 * @param args
	 */
	public ClientActor(ActorRef inRemoteServer) {

		remoteServer = inRemoteServer;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String) message;
			remoteServer.tell(msg);
		}
	}

	@Override
	public void preStart() {
		start = System.currentTimeMillis();
	}

	@Override
	public void postStop() {
		// tell the world that the calculation is complete
		long timeSpent = (System.currentTimeMillis() - start) / 1000;
		System.out
				.println(String
						.format("\n\tClientActor estimate: \t\t\n\tCalculation time: \t%s Secs",
								timeSpent));
	}
}
