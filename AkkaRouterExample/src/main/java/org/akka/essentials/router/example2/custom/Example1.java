package org.akka.essentials.router.example2.custom;

import java.util.Arrays;

import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RandomRouter;

public class Example1 {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ActorSystem _system = ActorSystem.create("CustomRouteeRouterExample");
		
		ActorRef echoActor1 = _system.actorOf(new Props(MsgEchoActor.class));
		ActorRef echoActor2 = _system.actorOf(new Props(MsgEchoActor.class));
		ActorRef echoActor3 = _system.actorOf(new Props(MsgEchoActor.class));
		
		Iterable<ActorRef> routees = Arrays.asList(new ActorRef[] { echoActor1,
				echoActor2, echoActor3 });

		ActorRef randomRouter = _system.actorOf(new Props(MsgEchoActor.class)
				.withRouter(RandomRouter.create(routees)));

		for (int i = 1; i <= 10; i++) {
			// sends randomly to actors
			randomRouter.tell(i);
		}
		_system.shutdown();
	}

}
