package org.akka.essentials.router.example2.config;

import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;

import com.typesafe.config.ConfigFactory;

public class Example {
	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ActorSystem _system = ActorSystem.create("RandomRouterExample",
				ConfigFactory.load().getConfig("MyRouterExample"));
		ActorRef randomRouter = _system.actorOf(
				new Props(MsgEchoActor.class).withRouter(new FromConfig()),
				"myRandomRouterActor");

		for (int i = 1; i <= 10; i++) {
			// sends randomly to actors
			randomRouter.tell(i);
		}
		_system.shutdown();
	}
}
