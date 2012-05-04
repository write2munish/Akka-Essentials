package org.akka.essentials.zeromq.example3.client;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import akka.routing.RoundRobinRouter;

public class MyClientActorSystem implements Bootable {

	ActorSystem system = null;

	public MyClientActorSystem() {
		try {
			system = ActorSystem.create("zeromqClientTest");
			system.actorOf(new Props(ClientActor.class)
					.withRouter(new RoundRobinRouter(3)), "client");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyClientActorSystem();
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

}
