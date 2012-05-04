package org.akka.essentials.zeromq.example4;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

public class MyActorSystem implements Bootable {

	ActorSystem system = null;

	public MyActorSystem() {
		system = ActorSystem.create("zeromqTest");
		system.actorOf(new Props(PushActor.class), "push");
		system.actorOf(new Props(PullActor1.class), "pull1");
		system.actorOf(new Props(PullActor2.class), "pull2");
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
