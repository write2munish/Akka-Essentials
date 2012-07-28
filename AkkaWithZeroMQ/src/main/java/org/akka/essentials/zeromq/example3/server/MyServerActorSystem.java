package org.akka.essentials.zeromq.example3.server;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class MyServerActorSystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("zeromqServerTest");
		system.actorOf(new Props(ServerActor.class), "server");
	}

}
