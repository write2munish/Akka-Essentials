package org.akka.essentials.loadgenerator;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;


public class ApplicationManagerSystem  {

	private ActorSystem system;
	private final ActorRef router;
	private final static int no_of_msgs = 10 * 1000000;

	public ApplicationManagerSystem() {

		final int no_of_workers = 10;

		system = ActorSystem.create("LoadGeneratorApp");

		final ActorRef appManager = system.actorOf(
				new Props(new UntypedActorFactory() {
					public UntypedActor create() {
						return new JobControllerActor(no_of_msgs);
					}
				}), "jobController");

		router = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new WorkerActor(appManager);
			}
		}).withRouter(new RoundRobinRouter(no_of_workers)));
	}

	private void generateLoad() {
		for (int i = no_of_msgs; i >= 0; i--) {
			router.tell("Job Id " + i + "# send");
		}
		System.out.println("All jobs sent successfully");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create the object and generate the load
		new ApplicationManagerSystem().generateLoad();

	}

}
