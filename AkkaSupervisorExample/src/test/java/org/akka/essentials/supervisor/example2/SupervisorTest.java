package org.akka.essentials.supervisor.example2;

import java.util.concurrent.TimeUnit;

import org.akka.essentials.supervisor.example2.MyActorSystem2.Result;
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

import com.typesafe.config.ConfigFactory;

public class SupervisorTest extends TestKit {
	static ActorSystem _system = ActorSystem.create("faultTolerance",
			ConfigFactory.load().getConfig("SupervisorSys"));
	TestActorRef<SupervisorActor2> supervisor = TestActorRef.apply(new Props(
			SupervisorActor2.class), _system);

	public SupervisorTest() {
		super(_system);
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
		supervisor.tell(Integer.valueOf(8));
		supervisor.tell(Integer.valueOf(-8));
		
		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(8));
	}

	@Test
	public void restartTest() throws Exception {
		_system.scheduler().scheduleOnce(
				Duration.create(0, TimeUnit.MILLISECONDS), supervisor,
				"null", _system.dispatcher());
		
		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(0));
	}

	@Test
	public void stopTest() throws Exception {

		ActorRef workerActor1 = supervisor.underlyingActor().workerActor1;
		ActorRef workerActor2 = supervisor.underlyingActor().workerActor2;

		TestProbe probe1 = new TestProbe(_system);
		TestProbe probe2 = new TestProbe(_system);
		probe1.watch(workerActor1);
		probe2.watch(workerActor2);

		supervisor.tell(Long.parseLong("10"));

		probe1.expectMsgClass(Terminated.class);
		probe2.expectMsgClass(Terminated.class);
	}
}
