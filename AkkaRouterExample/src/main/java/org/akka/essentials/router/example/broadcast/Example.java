package org.akka.essentials.router.example.broadcast;

import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.BroadcastRouter;

public class Example {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem _system = ActorSystem.create("BroadcastRouterExample");
		ActorRef broadcastRouter = _system.actorOf(new Props(MsgEchoActor.class)
				.withRouter(new BroadcastRouter(5)),"myBroadcastRouterActor");

		for (int i = 1; i <= 2; i++) {
			//same message goes to all the actors
			broadcastRouter.tell(i);
		}
		_system.shutdown();

	}

}
