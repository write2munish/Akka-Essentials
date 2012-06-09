package org.akka.essentials.router.example.roundrobin;

import org.akka.essentials.router.example.MsgEchoActor;

import scala.actors.threadpool.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

public class Example {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		ActorSystem _system = ActorSystem.create("RoundRobinRouterExample");
		ActorRef roundRobinRouter = _system.actorOf(new Props(
				MsgEchoActor.class).withRouter(new RoundRobinRouter(5)),"myRoundRobinRouterActor");

		for (int i = 1; i <= 10; i++) {
			//sends messages in a round robin way to all the actors
			roundRobinRouter.tell(i);
			if (i == 5) {
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println("\n");
			}
		}
		_system.shutdown();
	}

}
