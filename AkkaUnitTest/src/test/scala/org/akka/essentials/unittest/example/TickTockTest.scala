package org.akka.essentials.unittest.example

import org.junit.Assert
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.dispatch.Await
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.duration.intToDurationInt
import akka.util.Timeout
import junit.framework.TestCase
import org.junit.runner.RunWith

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
    actor.tock(new Tock("some message"))
    Assert.assertTrue(actor.state)
  }

  def testTwo() = {
    val actorRef = TestActorRef[TickTock]

    implicit val timeout = Timeout(5 seconds)
    val future = (actorRef ? new Tick("msg")).mapTo[String]
    val result = Await.result(future, timeout.duration)

    Assert.assertEquals("processed the tick message", result)
  }

  def testThree() = {
    val actorRef = TestActorRef[TickTock]

    try {
      actorRef.receive("do something")
      //should not reach here
      Assert.fail()
    } catch {
      case e: IllegalArgumentException => Assert.assertEquals(e.getMessage(), "boom!")
    }
  }
}