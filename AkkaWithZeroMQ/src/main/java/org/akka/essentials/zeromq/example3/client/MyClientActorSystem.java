package org.akka.essentials.zeromq.example3.client;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

public class MyClientActorSystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("zeromqClientTest");
		system.actorOf(new Props(ClientActor.class)
				.withRouter(new RoundRobinRouter(3)), "client");
	}

}
