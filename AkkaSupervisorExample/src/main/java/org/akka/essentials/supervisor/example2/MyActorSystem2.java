package org.akka.essentials.supervisor.example2;

import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;


public class MyActorSystem2 {

	public static class Result {
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem.create("faultTolerance");

		LoggingAdapter log = Logging.getLogger(system, system);

		Integer originalValue = Integer.valueOf(0);

		ActorRef supervisor = system.actorOf(new Props(SupervisorActor2.class),
				"supervisor");

		log.info("Sending value 8, no exceptions should be thrown! ");
		supervisor.tell(Integer.valueOf(8));

		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		log.info("Value Received-> {}", result);
		assert result.equals(Integer.valueOf(8));

		log.info("Sending value -8, ArithmeticException should be thrown! Our Supervisor strategy says resume !");
		supervisor.tell(Integer.valueOf(-8));

		result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		log.info("Value Received-> {}", result);
		assert result.equals(Integer.valueOf(8));

		log.info("Sending value null, NullPointerException should be thrown! Our Supervisor strategy says restart !");
		supervisor.tell(null);

		result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		log.info("Value Received-> {}", result);
		assert originalValue.equals(result);

		log.info("Sending value \"String\", IllegalArgumentException should be thrown! Our Supervisor strategy says Stop !");

		supervisor.tell(String.valueOf("Do Something"));

		log.info("Worker Actor shutdown !");
		system.shutdown();

	}

}
