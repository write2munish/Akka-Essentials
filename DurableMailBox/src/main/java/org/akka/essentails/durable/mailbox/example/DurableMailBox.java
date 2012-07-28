package org.akka.essentails.durable.mailbox.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.typesafe.config.ConfigFactory;

public class DurableMailBox extends UntypedActor {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ActorSystem system = ActorSystem.create("DurableMailBox", ConfigFactory
				.load().getConfig("myapp1"));

		ActorRef actor = system
				.actorOf(new Props(DurableMailBox.class)
						.withDispatcher("my-dispatcher"), "serverActor");

		actor.tell("Hello");

		Thread.sleep(500);

		system.shutdown();

	}

	@Override
	public void onReceive(Object message) throws Exception {
		System.out.println("Received message " + message);
	}

}
