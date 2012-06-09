package org.akka.essentials.router.custom.example;

import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Example {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem _system = ActorSystem.create("CustomRouterExample");
		ActorRef burstyMessageRouter = _system.actorOf(new Props(
				MsgEchoActor.class).withRouter(new BurstyMessageRouter(5,2)));

		for (int i = 1; i <= 13; i++) {
			//sends series of messages in a round robin way to all the actors
			burstyMessageRouter.tell(i);
		}
		_system.shutdown();

	}

}
