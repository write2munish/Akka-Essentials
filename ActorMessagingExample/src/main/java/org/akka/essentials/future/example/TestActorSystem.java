package org.akka.essentials.future.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class TestActorSystem {

	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("FutureUsageExample");
		ActorRef processOrder = _system.actorOf(new Props(
				ProcessOrderActor.class));
		processOrder.tell(Integer.valueOf(456));

		Thread.sleep(5000);

		_system.shutdown();
	}

}
