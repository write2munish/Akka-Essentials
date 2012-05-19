package org.akka.essentials.unittest.example

import org.akka.essentials.unittest.example.TickTock
import org.junit.Assert

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props
import akka.dispatch.Await
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.duration.intToDurationInt
import akka.util.Timeout
import junit.framework.TestCase

class TickTockTest extends TestCase {

  implicit val system = ActorSystem("TestSys", ConfigFactory
    .load().getConfig("TestSys"))

  override def setUp() = {
  }

  override def tearDown() = {
  }

  def testOne() = {
    val actorRef = TestActorRef[TickTock]

    // get access to the underlying actor object
    val actor: TickTock = actorRef.underlyingActor
    // access the methods the actor object and directly pass arguments and
    // test
    actor.tock_this("tock something")
    Assert.assertTrue(actor.state);

    actor.tock_this("once again");
    Assert.assertFalse(actor.state);
  }

  def testTwo() = {
    val actorRef = TestActorRef[TickTock]

    implicit val timeout = Timeout(5 seconds)
    val future = actorRef ? new Tick()
    val result = Await.result(future, timeout.duration).asInstanceOf[String]

    Assert.assertEquals("processed the tick message", result)
  }
}