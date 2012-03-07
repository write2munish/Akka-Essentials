package org.akka.essentials.clientserver.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class ServerActorSystem implements Bootable {

	private ActorSystem system;

	public ServerActorSystem() {
		system = ActorSystem.create("HelloWorldApplication", ConfigFactory
				.load().getConfig("ServerActor"));
		ActorRef actor = system.actorOf(new Props(ServerActor.class),
				"serverActor");
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

		new ServerActorSystem();
		
	}

}
