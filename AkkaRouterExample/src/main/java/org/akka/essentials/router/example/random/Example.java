package org.akka.essentials.router.example.random;

import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RandomRouter;

public class Example {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ActorSystem _system = ActorSystem.create("RandomRouterExample");
		ActorRef randomRouter = _system.actorOf(new Props(MsgEchoActor.class)
				.withRouter(new RandomRouter(5)),"myRandomRouterActor");

		for (int i = 1; i <= 10; i++) {
			//sends randomly to actors
			randomRouter.tell(i);
		}
		_system.shutdown();
	}

}
