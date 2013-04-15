package org.akka.essentials.grid.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.kernel.Bootable;
import akka.remote.RemoteLifeCycleEvent;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class WorkServerActorSystem implements Bootable {

	private LoggingAdapter log = null;
	private ActorSystem system;
	private ActorRef workSchedulerActor;
	private ActorRef jobControllerActor;
	@SuppressWarnings("unused")
	private ActorRef registerRemoteWorkerActor;
	private ActorRef remoteActorListener;

	/*
	 * default constructor
	 */
	@SuppressWarnings("serial")
	public WorkServerActorSystem() {
		// load the configuration
		system = ActorSystem.create("WorkServerSys", ConfigFactory.load()
				.getConfig("WorkServerSys"));
		log = Logging.getLogger(system, this);

		// create the work scheduler actor
		workSchedulerActor = system.actorOf(
				new Props(WorkSchedulerActor.class), "WorkSchedulerActor");

		// create the job controller actor, which manages the routees and sends
		// out
		// work packets to the registered workers
		jobControllerActor = system.actorOf(new Props(
				new UntypedActorFactory() {
					public UntypedActor create() {
						return new JobControllerActor(workSchedulerActor);
					}
				}), "JobControllerActor");
		
		remoteActorListener = system.actorOf(new Props(
				new UntypedActorFactory() {
					public UntypedActor create() {
						return new RemoteClientEventListener(jobControllerActor);
					}
				}), "RemoteClientEventListener");


		// actor that registers and unregisters the workers
		registerRemoteWorkerActor = system.actorOf(new Props(
				new UntypedActorFactory() {
					public UntypedActor create() {
						return new RegisterRemoteWorkerActor(jobControllerActor);
					}
				}), "RegisterRemoteWorkerActor");

		workSchedulerActor.tell("Start Sending Work", jobControllerActor);

		system.eventStream().subscribe(remoteActorListener,
				RemoteLifeCycleEvent.class);

	}

	public void shutdown() {
		log.info("Shutting down the ServerActorSystem");

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

		new WorkServerActorSystem();

	}

	/**
	 * Create a unbounded priority mailbox to make sure that the display_list
	 * message has the least priority. The standard text messages get processed
	 * earlier than that.
	 * 
	 * @author Munish
	 * 
	 */
	public static class MyUnboundedPriorityMailbox extends
			UnboundedPriorityMailbox {

		public MyUnboundedPriorityMailbox(ActorSystem.Settings settings,
				Config config) {

			// Creating a new PriorityGenerator,
			super(new PriorityGenerator() {
				@Override
				public int gen(Object message) {
					if (message instanceof Address)
						return 0; // Worker Registration messages should be
									// treated
									// with highest priority
					else if (message.equals(PoisonPill.getInstance()))
						return 3; // PoisonPill when no other left
					else
						return 1; // By default they go with medium priority
				}
			});
		}
	}
}
