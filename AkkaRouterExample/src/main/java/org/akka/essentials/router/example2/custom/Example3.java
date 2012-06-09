package org.akka.essentials.router.example2.custom;

import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.DefaultResizer;
import akka.routing.RandomRouter;

public class Example3 {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ActorSystem _system = ActorSystem.create("CustomRouteeRouterExample");

		int lowerBound = 2;
		int upperBound = 15;
		DefaultResizer resizer = new DefaultResizer(lowerBound, upperBound);

		ActorRef randomRouter = _system.actorOf(new Props(MsgEchoActor.class)
				.withRouter(new RandomRouter(resizer)));

		for (int i = 1; i <= 10; i++) {
			// sends randomly to actors
			randomRouter.tell(i);
		}
		_system.shutdown();
	}

}
