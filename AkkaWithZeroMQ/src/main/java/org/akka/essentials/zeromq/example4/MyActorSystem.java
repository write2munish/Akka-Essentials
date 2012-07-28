package org.akka.essentials.zeromq.example4;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class MyActorSystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("zeromqTest");
		system.actorOf(new Props(PushActor.class), "push");
		system.actorOf(new Props(PullActor1.class), "pull1");
		system.actorOf(new Props(PullActor2.class), "pull2");
	}

}
