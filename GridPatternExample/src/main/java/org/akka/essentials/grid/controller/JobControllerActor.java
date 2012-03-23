package org.akka.essentials.grid.controller;

import java.util.ArrayList;
import java.util.List;

import org.akka.essentials.grid.worker.WorkerActor;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RemoteRouterConfig;
import akka.routing.RoundRobinRouter;

public class JobControllerActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef workerRouterActor = null;
	private List<Address> workerAddress = new ArrayList<Address>();

	private static int count = 1;
	private ActorRef workSchedulerActor;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Address) {

			log.info("Recieved worker registration request");

			addWorkerRoute((Address) message);

			log.info("worker router updated for the new address");

		} else if (message instanceof String) {
			if (((String) message).compareTo("SendWork") == 0) {
				log.info("About to send work packets to workers");
				if (workerRouterActor != null) {
					for (int i = 0; i < workerAddress.size(); i++) {
						workerRouterActor.tell("Work Packet Id#" + count++,
								this.getSelf());
					}
					log.info("Work Packets send");
					//let the workscheduler know that you finished with sending work packets
					workSchedulerActor.tell("Work Packets send", getContext()
							.self());
				} else {
					log.info("No workers registered as of now!");
					
					//tell the workscheduler actor to wake me up in 5 secs again
					workSchedulerActor.tell(Integer.valueOf(5), getContext()
							.self());
				}
				
			} else if (((String) message).compareTo("Enough!") == 0) {
				log.info("Worker requesting shutdown"
						+ getSender().path().toString());
				//TODO shut down the routee at this point and remote the routerconfig
				
			}
		}

	}

	public JobControllerActor(ActorRef inWorkSchedulerActor) {
		workSchedulerActor = inWorkSchedulerActor;
	}

	/**
	 * Add the new worker to the router mechanism
	 * 
	 * @param address
	 */
	private void addWorkerRoute(Address address) {

		//send the stop message to all the worker actors
		if (workerRouterActor != null) {
			for (int i = 0; i < workerAddress.size(); i++)
				workerRouterActor.tell("STOP");
		}
		//add the address to the arraylist
		workerAddress.add(address);

		//update the workerRouter actor with the information on all workers
		Address[] addressNodes = new Address[workerAddress.size()];

		workerRouterActor = getContext().system().actorOf(
				new Props(WorkerActor.class).withRouter(new RemoteRouterConfig(
						new RoundRobinRouter(workerAddress.size()),
						workerAddress.toArray(addressNodes))));

	}

}
