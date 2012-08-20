package org.akka.essentials.unittest.example

import java.util.Random
import org.junit.runner.RunWith
import org.junit.Assert
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.matchers.MustMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.WordSpec
import com.typesafe.config.ConfigFactory
import akka.actor.actorRef2Scala
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Terminated
import akka.dispatch.Await
import akka.pattern.ask
import akka.testkit.CallingThreadDispatcher
import akka.testkit.ImplicitSender
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.testkit.TestProbe
import akka.util.duration.intToDurationInt
import akka.util.Timeout
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExampleUnitTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpec with MustMatchers with BeforeAndAfterAll with ShouldMatchersForJUnit {

  def this() = this(ActorSystem("TestSys", ConfigFactory
    .load().getConfig("TestSys")))

  "Test Echo actor" must {
    "send back messages unchanged" in {
      val echo = system.actorOf(Props[EchoActor])
      echo ! "Hi there"
      expectMsg("Hi there")
    }
  }

  "Test Forwarding actor" must {
    "forwards the messages unchanged to another actor" in {
      val forwarding = system.actorOf(Props(new ForwardingActor(this.testActor)))
      forwarding ! "test message"
      expectMsg("test message")
    }
  }

  "Test Filtering actor" must {
    "filters out messages" in {
      val filtering = system.actorOf(Props(new FilteringActor(this.testActor)))
      filtering ! "test message"
      expectMsg("test message")
      filtering ! 1
      expectNoMsg
    }
  }

  "Test Sequencing actor" must {
    "checks for one message" in {
      val randomHead = new Random().nextInt(6)
      val randomTail = new Random().nextInt(10)

      val headList = List().padTo(randomHead, new Integer(0))
      val tailList = List().padTo(randomTail, new Integer(1))

      val sequencing = system.actorOf(Props(new SequencingActor(this.testActor, headList, tailList)), name = "Sequencing")
      sequencing ! "test message"
      ignoreMsg {
        case msg: Integer => msg != Integer.valueOf(100)
      }
      expectMsg("test message")
      ignoreMsg {
        case msg: Integer => msg == Integer.valueOf(1)
      }
      expectNoMsg
    }
  }

  "Test Supervisor Strategy 1" must {
    "checks for resumed workers" in {
      implicit val timeout = Timeout(5 seconds)

      val supervisor = system.actorOf(Props[SupervisorActor])
      val future = (supervisor ? Props[BoomActor]).mapTo[ActorRef]
      val child = Await.result(future, timeout.duration)

      child.tell(Integer.valueOf(123))

      Assert.assertFalse(child.isTerminated)

    }
  }

  "Test Supervisor Strategy 2" must {
    "checks for terminated workers" in {
      implicit val timeout = Timeout(5 seconds)

      val supervisor = system.actorOf(Props[SupervisorActor])
      val probe = TestProbe()

      val future = (supervisor ? Props[BoomActor]).mapTo[ActorRef]
      val child = Await.result(future, timeout.duration)

      probe.watch(child)

      child.tell("do something")
      probe.expectMsg(Terminated(child))

    }
  }

  "Test Boom Actor" must {
    "checks for exceptions from Actor" in {

      val boom = TestActorRef[BoomActor]

      try {
        boom.receive("do something")
        //should not reach here
        Assert.assertTrue(false)
      } catch {
        case e: IllegalArgumentException => Assert.assertEquals(e.getMessage(), "boom!")
      }
    }
  }

  override def afterAll {
    system.shutdown()
  }
}

