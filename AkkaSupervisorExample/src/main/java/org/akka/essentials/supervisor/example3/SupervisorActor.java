package org.akka.essentials.supervisor.example3;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import akka.util.Duration;

public class SupervisorActor extends UntypedActor {
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	public ActorRef workerActor = getContext().actorOf(
			new Props(WorkerActor.class), "workerActor");

	ActorRef monitor = getContext().system().actorOf(
			new Props(MonitorActor.class), "monitorActor");

	@Override
	public void preStart() {
		//MyActorSystem.monitor.tell(new RegisterWorker(workerActor, self()));
		monitor.tell(new RegisterWorker(workerActor, self()));
	}

	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
			Duration.parse("10 second"), new Function<Throwable, Directive>() {
				public Directive apply(Throwable t) {
					if (t instanceof ArithmeticException) {
						return resume();
					} else if (t instanceof NullPointerException) {
						return restart();
					} else if (t instanceof IllegalArgumentException) {
						return stop();
					} else {
						return escalate();
					}
				}
			});

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	public void onReceive(Object o) throws Exception {
		if (o instanceof Result) {
			workerActor.tell(o, getSender());
		} else if (o instanceof DeadWorker) {
			log.info("Got a DeadWorker message, restarting the worker");
			workerActor = getContext().actorOf(new Props(WorkerActor.class),
					"workerActor");
		} else
			workerActor.tell(o);
	}

	public ActorRef getWorker(){
		return workerActor;
	}
}
