package org.akka.essentials.zeromq.example1;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

public class MyActorSystem implements Bootable {

	ActorSystem system = null;

	public MyActorSystem() {
		system = ActorSystem.create("zeromqTest");
		system.actorOf(new Props(WorkerTaskA.class), "workerA");
		system.actorOf(new Props(WorkerTaskB.class), "workerB");
		system.actorOf(new Props(PublisherActor.class), "publisher");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyActorSystem();
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

}
