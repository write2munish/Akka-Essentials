package org.akka.essentials.router.custom.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.dispatch.Dispatchers;
import akka.routing.CustomRoute;
import akka.routing.CustomRouterConfig;
import akka.routing.Destination;
import akka.routing.RouteeProvider;

/**
 * Router that sends burst of packets to one actor before sending 
 * to the next. The packet burst is a configurable value 
 * 
 * @author Munish
 *
 */
public class BurstyMessageRouter extends CustomRouterConfig {

	int noOfInstances;
	int messageBurst;

	public BurstyMessageRouter(int inNoOfInstances, int inMessageBurst) {
		noOfInstances = inNoOfInstances;
		messageBurst = inMessageBurst;
	}

	public String routerDispatcher() {
		return Dispatchers.DefaultDispatcherId();
	}

	public SupervisorStrategy supervisorStrategy() {
		return SupervisorStrategy.defaultStrategy();
	}

	@Override
	public CustomRoute createCustomRoute(Props props,
			RouteeProvider routeeProvider) {

		// create the arraylist for holding the actors
		final List<ActorRef> routees = new ArrayList<ActorRef>(noOfInstances);
		for (int i = 0; i < noOfInstances; i++) {
			// initialize the actors and add to the arraylist
			routees.add(routeeProvider.context().actorOf(props));
		}
		// register the list
		routeeProvider.registerRoutees(routees);

		return new CustomRoute() {
			int messageCount = 0;
			int actorSeq = 0;
			ActorRef actor = routees.get(actorSeq);

			public Iterable<Destination> destinationsFor(ActorRef sender,
					Object message) {
				List<Destination> destinationList = Arrays
						.asList(new Destination[] { new Destination(sender,
								actor) });
				//increment message count
				messageCount++;
				//check message count
				if (messageCount == messageBurst) {
					actorSeq++;
					//reset the counter
					messageCount = 0;
					//reset actorseq counter
					if (actorSeq == noOfInstances) {
						actorSeq = 0;
					}
					actor = routees.get(actorSeq);
				}
				return destinationList;
			}
		};
	}
}
