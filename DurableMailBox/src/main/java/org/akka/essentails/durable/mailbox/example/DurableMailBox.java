package org.akka.essentails.durable.mailbox.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class DurableMailBox extends UntypedActor implements Bootable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ActorSystem system = ActorSystem.create("DurableMailBox", ConfigFactory
				.load().getConfig("myapp1"));
		
		ActorRef actor = system.actorOf(new Props(DurableMailBox.class).withDispatcher("my-dispatcher"),
				"serverActor");
		
		actor.tell("Hello");

	}

	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	public void startup() {
		// TODO Auto-generated method stub
		
	}

}
