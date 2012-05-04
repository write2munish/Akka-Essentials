package org.akka.essentials.supervisor.example3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.kernel.Bootable;

public class MyActorSystem implements Bootable {

	public static ActorRef monitor;

	LoggingAdapter log = null;
	ActorSystem system = null;

	@SuppressWarnings({ "static-access" })
	public MyActorSystem() throws Exception {
		system = ActorSystem.create("faultTolerance");

		log = Logging.getLogger(system, this);

		final ActorRef supervisor = system.actorOf(new Props(
				SupervisorActor.class), "supervisor");

		supervisor.tell(Integer.valueOf(10));
		supervisor.tell("10");

		Thread.currentThread().sleep(4000);

		supervisor.tell(Integer.valueOf(10));

		system.shutdown();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		MyActorSystem myActorSystem = null;
		try {
			myActorSystem = new MyActorSystem();
		} catch (Exception e) {
			myActorSystem.shutdown();
		}
	}

	public void shutdown() {
		system.shutdown();
	}

	public void startup() {

	}

}
