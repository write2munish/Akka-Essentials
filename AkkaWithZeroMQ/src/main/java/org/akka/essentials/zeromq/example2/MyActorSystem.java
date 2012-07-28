package org.akka.essentials.zeromq.example2;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class MyActorSystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("zeromqTest");
		system.actorOf(new Props(WorkerTaskA.class), "workerA");
		system.actorOf(new Props(WorkerTaskB.class), "workerB");
		system.actorOf(new Props(RouterActor.class), "router");
	}

}
