package org.akka.essentials.router.example2.custom;

import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.routing.RandomRouter;
import akka.routing.RemoteRouterConfig;

public class Example2 {
	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ActorSystem _system = ActorSystem.create("RemoteRouteeRouterExample");
		
		Address addr1 = new Address("akka", "remotesys", "127.0.0.1", 2552);
		Address addr2 = new Address("akka", "remotesys", "127.0.0.1", 2552);

		Address[] addresses = new Address[] { addr1, addr2 };
		
		ActorRef randomRouter = _system.actorOf(new Props(MsgEchoActor.class)
				.withRouter(new RemoteRouterConfig(new RandomRouter(5),addresses)));

		for (int i = 1; i <= 10; i++) {
			// sends randomly to actors
			randomRouter.tell(i);
		}
		_system.shutdown();
	}

}
