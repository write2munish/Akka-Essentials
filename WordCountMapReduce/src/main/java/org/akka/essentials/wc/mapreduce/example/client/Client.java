package org.akka.essentials.wc.mapreduce.example.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class Client implements Bootable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final String fileName = "Othello.txt";

		ActorSystem system = ActorSystem.create("ClientApplication",
				ConfigFactory.load().getConfig("WCMapReduceClientApp"));

		final ActorRef fileReadActor = system.actorOf(new Props(
				FileReadActor.class));

		final ActorRef remoteActor = system
				.actorFor("akka://WCMapReduceApp@127.0.0.1:2552/user/WCMapReduceActor");

		@SuppressWarnings("serial")
		ActorRef actor = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new ClientActor(remoteActor);
			}
		}));

		fileReadActor.tell(fileName,actor);

		remoteActor.tell("DISPLAY_LIST");

		system.shutdown();

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

}
