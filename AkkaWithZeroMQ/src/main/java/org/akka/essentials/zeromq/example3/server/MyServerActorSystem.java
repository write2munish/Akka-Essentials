package org.akka.essentials.zeromq.example3.server;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

public class MyServerActorSystem implements Bootable {

	ActorSystem system = null;

	public MyServerActorSystem() {
		try {
			system = ActorSystem.create("zeromqServerTest");
			system.actorOf(new Props(ServerActor.class), "server");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyServerActorSystem();
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

}
