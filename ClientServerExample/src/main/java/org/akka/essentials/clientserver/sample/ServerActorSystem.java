package org.akka.essentials.clientserver.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class ServerActorSystem implements Bootable {

	private LoggingAdapter log = null;
	private ActorSystem system;

	/*
	 * default constructor
	 */
	public ServerActorSystem() {
		// load the configuration
		system = ActorSystem.create("ServerSys", ConfigFactory.load()
				.getConfig("ServerSys"));
		log = Logging.getLogger(system, this);
		// create the actor
		@SuppressWarnings("unused")
		ActorRef actor = system.actorOf(new Props(ServerActor.class),
				"serverActor");
	}

	public void shutdown() {
		log.info("Shutting down the ServerActorSystem");

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

		new ServerActorSystem();

	}

}
