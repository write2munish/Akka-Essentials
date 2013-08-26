package org.akka.essentials.wc.mapreduce.example.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import akka.kernel.Bootable;
import akka.routing.RoundRobinRouter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class WCMapReduceServer implements Bootable {

	private ActorRef mapRouter;
	private ActorRef reduceRouter;
	private ActorRef aggregateActor;
	private ActorSystem system;
	@SuppressWarnings("unused")
	private ActorRef wcMapReduceActor;

	@SuppressWarnings("serial")
	public WCMapReduceServer(int no_of_reduce_workers, int no_of_map_workers) {

		system = ActorSystem.create("WCMapReduceApp", ConfigFactory.load()
				.getConfig("WCMapReduceApp"));

		// create the aggregate Actor
		aggregateActor = system.actorOf(new Props(AggregateActor.class));

		// create the list of reduce Actors
		reduceRouter = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new ReduceActor(aggregateActor);
			}
		}).withRouter(new RoundRobinRouter(no_of_reduce_workers)));

		// create the list of map Actors
		mapRouter = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new MapActor(reduceRouter);
			}
		}).withRouter(new RoundRobinRouter(no_of_map_workers)));

		// create the overall WCMapReduce Actor that acts as the remote actor
		// for clients
		wcMapReduceActor = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new WCMapReduceActor(aggregateActor, mapRouter);
			}
		}).withDispatcher("priorityMailBox-dispatcher"), "WCMapReduceActor");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new WCMapReduceServer(5, 5);

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	/**
	 * Create a unbounded priority mailbox to make sure that the display_list message 
	 * has the least priority. The standard text messages get processed earlier than that.
	 * 
	 * @author Munish
	 *
	 */
	public static class MyPriorityMailBox extends UnboundedPriorityMailbox {

		public MyPriorityMailBox(ActorSystem.Settings settings, Config config) {

			// Creating a new PriorityGenerator,
			super(new PriorityGenerator() {
				@Override
				public int gen(Object message) {
					if (message.equals("DISPLAY_LIST"))
						return 2; // 'DisplayList messages should be treated
									// last if possible
					else if (message.equals(PoisonPill.getInstance()))
						return 3; // PoisonPill when no other left
					else
						return 0; // By default they go with high priority
				}
			});
		}

	}
}
