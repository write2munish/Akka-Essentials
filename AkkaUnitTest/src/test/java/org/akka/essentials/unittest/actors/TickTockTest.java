package org.akka.essentials.unittest.actors;

import static akka.pattern.Patterns.ask;
import junit.framework.Assert;

import org.akka.essentials.unittest.actors.TickTock;
import org.akka.essentials.unittest.actors.TickTock.Tick;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Await;
import akka.testkit.TestActorRef;
import akka.util.Duration;

import com.typesafe.config.ConfigFactory;

public class TickTockTest {
	static ActorSystem _system = ActorSystem.create("TestSys", ConfigFactory
			.load().getConfig("TestSys"));
	@Test
	public void tockTest() {
		TestActorRef<TickTock> actorRef = TestActorRef.apply(new Props(
				TickTock.class), _system);

		// get access to the underlying actor object
		TickTock actor = actorRef.underlyingActor();
		// access the methods the actor object and directly pass arguments and
		// test
		actor.tock_this("tock something");
		Assert.assertTrue(actor.state);

		actor.tock_this("once again");
		Assert.assertFalse(actor.state);
	}

	@Test
	public void tickTest() throws Exception {
		TestActorRef<TickTock> actorRef = TestActorRef.apply(new Props(
				TickTock.class), _system);

		String result = (String) Await.result(ask(actorRef, new Tick(), 5000),
				Duration.parse("5 second"));

		Assert.assertEquals("processed the tick message", result);
	}
}
