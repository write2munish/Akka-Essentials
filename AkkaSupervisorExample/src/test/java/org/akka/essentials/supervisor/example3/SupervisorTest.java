package org.akka.essentials.supervisor.example3;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.pattern.Patterns;
import akka.testkit.TestActorRef;
import akka.testkit.TestKit;
import akka.testkit.TestProbe;


public class SupervisorTest extends TestKit {
	static ActorSystem _system = ActorSystem.create("faultTolerance");

	public SupervisorTest() {
		super(_system);
	}

	@Test
	public void stopAndRestartTest() throws Exception {
		TestActorRef<SupervisorActor> supervisor = TestActorRef.apply(
				new Props(SupervisorActor.class), _system);
		ActorRef workerActor = supervisor.underlyingActor().getWorker();
		TestProbe probe = new TestProbe(_system);
		probe.watch(workerActor);
		supervisor.tell("10");
		probe.expectMsgClass(Terminated.class);

		Thread.sleep(2000);
		// the actor should get restarted
		// lets send a new value and retrieve the same
		supervisor.tell(Integer.valueOf(10));
		
		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(10));
	}

}
