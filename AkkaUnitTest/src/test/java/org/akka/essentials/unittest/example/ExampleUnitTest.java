package org.akka.essentials.unittest.example;

import static akka.pattern.Patterns.ask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.akka.essentials.unittest.actors.BoomActor;
import org.akka.essentials.unittest.actors.EchoActor;
import org.akka.essentials.unittest.actors.FilteringActor;
import org.akka.essentials.unittest.actors.ForwardingActor;
import org.akka.essentials.unittest.actors.SequencingActor;
import org.akka.essentials.unittest.actors.SupervisorActor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.dispatch.Await;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.testkit.TestActorRef;
import akka.testkit.TestKit;
import akka.testkit.TestProbe;
import akka.util.Duration;

import com.typesafe.config.ConfigFactory;

public class ExampleUnitTest extends TestKit {

	static ActorSystem _system = ActorSystem.create("TestSys", ConfigFactory
			.load().getConfig("TestSys"));

	LoggingAdapter log = Logging.getLogger(_system, this);

	public ExampleUnitTest() {
		super(_system);
	}

	@Before
	public void init() {

	}

	@After
	public void shutdown() {

	}

	@Test
	public void testEchoActor() {
		ActorRef echoActorRef = _system.actorOf(new Props(EchoActor.class));
		// pass the reference to implicit sender testActor() otherwise
		// message end up in dead mailbox
		echoActorRef.tell("Hi there", super.testActor());
		expectMsg("Hi there");
	}

	@Test
	public void testForwardingActor() {
		ActorRef forwardingActorRef = _system.actorOf(new Props(
				new UntypedActorFactory() {
					public UntypedActor create() {
						return new ForwardingActor(testActor());
					}
				}));
		// pass the reference to implicit sender testActor() otherwise
		// message end up in dead mailbox
		forwardingActorRef.tell("test message", super.testActor());
		expectMsg("test message");
	}

	@Test
	public void testSequencingActor() {
		final List<Integer> headList = new ArrayList<Integer>();
		final List<Integer> tailList = new ArrayList<Integer>();

		int randomHead = new Random().nextInt(6);
		int randomTail = new Random().nextInt(10);

		for (int i = 0; i < randomHead; i++)
			headList.add(i);
		for (int i = 1; i < randomTail; i++)
			tailList.add(i);

		ActorRef sequencingActorRef = _system.actorOf(new Props(
				new UntypedActorFactory() {
					public UntypedActor create() {
						return new SequencingActor(testActor(), headList,
								tailList);
					}
				}));

		// pass the reference to implicit sender testActor() otherwise
		// message end up in dead mailbox
		sequencingActorRef.tell("do something", super.testActor());

		for (Integer value : headList) {
			expectMsgClass(Integer.class);
		}
		expectMsg("do something");
		for (Integer value : tailList) {
			expectMsgClass(Integer.class);
		}
		expectNoMsg();
	}

	@Test
	public void testFilteringActor() {
		ActorRef filteringActorRef = _system.actorOf(new Props(
				new UntypedActorFactory() {
					public UntypedActor create() {
						return new FilteringActor(testActor());
					}
				}));
		// pass the reference to implicit sender testActor() otherwise
		// message end up in dead mailbox
		// first test
		filteringActorRef.tell("test message", super.testActor());
		expectMsg("test message");
		// second test
		filteringActorRef.tell(1, super.testActor());
		expectNoMsg();
	}

	/**
	 * if you want to test how the Supervisor strategy is working fine
	 */
	@Test
	public void testSupervisorStrategy1() throws Exception {

		ActorRef supervisorActorRef1 = _system.actorOf(new Props(
				SupervisorActor.class), "supervisor1");

		Duration timeout = Duration.parse("5 second");
		// register the BoomActor with the Supervisor
		final ActorRef child = (ActorRef) Await.result(
				ask(supervisorActorRef1, new Props(BoomActor.class), 5000),
				timeout);

		child.tell(123);

		Assert.assertFalse(child.isTerminated());
	}

	@Test
	public void testSupervisorStrategy2() throws Exception {

		ActorRef supervisorActorRef2 = _system.actorOf(new Props(
				SupervisorActor.class), "supervisor2");
		
		final TestProbe probe = new TestProbe(_system);
		// register the BoomActor with the Supervisor
		final ActorRef child = (ActorRef) Await.result(
				ask(supervisorActorRef2, new Props(BoomActor.class), 5000),
				Duration.parse("5 second"));
		probe.watch(child);
		// second check
		child.tell("do something");
		probe.expectMsg(new Terminated(child));

	}

	@Test
	public void testBoomActor() {
		final TestActorRef child = TestActorRef.apply(
				new Props(BoomActor.class), _system);
		try {
			child.receive("do something");
			// should not reach here
			Assert.assertTrue(false);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals(e.getMessage(), "boom!");
		}
	}

}
