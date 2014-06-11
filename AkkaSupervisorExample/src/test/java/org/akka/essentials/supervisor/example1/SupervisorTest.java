package org.akka.essentials.supervisor.example1;

import java.util.concurrent.TimeUnit;

import org.akka.essentials.supervisor.example1.MyActorSystem.Result;
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
	TestActorRef<SupervisorActor> supervisor = TestActorRef.apply(new Props(
			SupervisorActor.class), _system);

	public SupervisorTest() {
		super(_system);
		supervisor.tell(Integer.valueOf(8));
	}

	@Test
	public void successTest() throws Exception {
		supervisor.tell(Integer.valueOf(8));

		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(8));
	}

	@Test
	public void resumeTest() throws Exception {
		TestActorRef<SupervisorActor> supervisor = TestActorRef.apply(
				new Props(SupervisorActor.class), _system);

		//first send a correct message
		supervisor.tell(Integer.valueOf(8));
		//Send a  message that generates exception
		supervisor.tell(Integer.valueOf(-8));

		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(8));
	}

	@Test
	public void restartTest() throws Exception {
		supervisor.tell("null");

		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(0));
	}

	@Test
	public void stopTest() throws Exception {

		ActorRef workerActor = supervisor.underlyingActor().childActor;
		TestProbe probe = new TestProbe(_system);
		probe.watch(workerActor);

		supervisor.tell(Long.parseLong("10"));

		probe.expectMsgClass(Terminated.class);
	}
}
