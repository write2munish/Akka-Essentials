package org.akka.essentials.supervisor.example3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class MyActorSystem {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem.create("faultTolerance");

		ActorRef supervisor = system.actorOf(new Props(SupervisorActor.class),
				"supervisor");

		supervisor.tell(Integer.valueOf(10));
		supervisor.tell("10");

		Thread.sleep(5000);

		supervisor.tell(Integer.valueOf(10));

		system.shutdown();
	}

}
