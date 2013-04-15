package org.akka.essentials.grid.controller;

import java.util.HashMap;
import java.util.Map;

import org.akka.essentials.grid.StartWorker;
import org.akka.essentials.grid.StopWorker;
import org.akka.essentials.grid.Task;
import org.akka.essentials.grid.TaskFinished;
import org.akka.essentials.grid.worker.WorkerActor;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.remote.routing.RemoteRouterConfig;
import akka.routing.RoundRobinRouter;

public class JobControllerActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef workerRouterActor = null;
	private Map<String, Address> workerAddressMap = new HashMap<String, Address>();

	private static int count = 0;
	private ActorRef workSchedulerActor;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof StartWorker) {

			log.info("Received worker registration request");

			addWorkerRoute(((StartWorker) message).getActorPath());

			log.info("worker router updated for the new address");

			workSchedulerActor.tell("You can start sending work!", getContext()
					.self());

		} else if (message instanceof String) {
			if (((String) message).compareTo("SendWork") == 0) {
				log.info("About to send work packets to workers");
				if (workerRouterActor != null) {
					for (int i = 0; i < workerAddressMap.size(); i++) {
						count++;
						workerRouterActor.tell(new Task(count), this.getSelf());
					}
					log.info("Work Packets send upto->" + count);
					// let the workscheduler know that you finished with sending
					// work packets
					workSchedulerActor.tell("Work Packets send", getContext()
							.self());
				} else {
					log.info("No workers registered as of now!");

					// tell the workscheduler actor to wake me up in 5 secs
					// again
					workSchedulerActor.tell(Integer.valueOf(5), getContext()
							.self());
				}

			} else if (((String) message).compareTo("Enough!") == 0) {
				log.info("Worker requesting shutdown"
						+ getSender().path().toString());
				// TODO shut down the routee at this point and remote the
				// routerconfig

			}
		} else if (message instanceof TaskFinished) {
			log.info("Task finished->"
					+ ((TaskFinished) message).getTaskNumber());
		} else if (message instanceof StopWorker) {
			StopWorker stopMe = (StopWorker) message;
			log.info("Worker requesting shutdown->" + stopMe.getActorPath());
			removeWorkerRoute(stopMe.getActorPath());
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
	private void addWorkerRoute(String address) {

		// send the stop message to all the worker actors
		if (workerRouterActor != null) {
			for (int i = 0; i < workerAddressMap.size(); i++)
				workerRouterActor.tell("STOP");
		}

		// add the address to the Map
		workerAddressMap.put(address, AddressFromURIString.parse(address));

		Address[] addressNodes = new Address[workerAddressMap.size()];

		Address[] workerAddress = workerAddressMap.values().toArray(
				addressNodes);

		// update the workerRouter actor with the information on all workers
		workerRouterActor = getContext().system().actorOf(
				new Props(WorkerActor.class).withRouter(new RemoteRouterConfig(
						new RoundRobinRouter(workerAddress.length),
						workerAddress)));

	}

	private void removeWorkerRoute(String address) {
		
		//check if the address is registered or not
		if (workerRouterActor != null) {
			if (!workerAddressMap.containsKey(address)) {
				return;
			}
		}
		log.info("Processing Worker shutdown request->" + address);

		if (workerRouterActor != null) {
			for (int i = 0; i < workerAddressMap.size(); i++)
				if (!workerRouterActor.isTerminated())
					workerRouterActor.tell("STOP");
		}
		if (workerAddressMap.size() > 0)
			workerAddressMap.remove(address);

		if (workerAddressMap.size() > 0) {
			Address[] addressNodes = new Address[workerAddressMap.size()];

			Address[] workerAddress = workerAddressMap.values().toArray(
					addressNodes);

			workerRouterActor = getContext().system().actorOf(
					new Props(WorkerActor.class)
							.withRouter(new RemoteRouterConfig(
									new RoundRobinRouter(workerAddress.length),
									workerAddress)));
		} else
			workerRouterActor = null;

	}

}
