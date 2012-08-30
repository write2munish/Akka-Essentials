package org.akka.essentials.calculator.example3
import org.akka.essentials.calculator.CalculatorInt

import akka.actor.SupervisorStrategy._
import akka.actor.SupervisorStrategy.Restart
import akka.actor.TypedActor.PostStop
import akka.actor.TypedActor.PreStart
import akka.actor.TypedActor.Supervisor
import akka.actor.ActorRef
import akka.actor.OneForOneStrategy
import akka.actor.Props
import akka.actor.SupervisorStrategy
import akka.actor.TypedActor
import akka.dispatch.Future
import akka.dispatch.Promise
import akka.event.Logging
import akka.util.duration.intToDurationInt

class SupervisorActor extends CalculatorInt with PreStart with PostStop with Supervisor {

  import TypedActor.dispatcher
  import TypedActor.context

  var counter: Int = 0
  val log = Logging(context.system, TypedActor.self.getClass())
  val childActor: ActorRef = context.actorOf(Props[ChildActor], name = "childActor")


  // Non blocking request response
  def add(first: Int, second: Int): Future[Int] = Promise successful first + second

  // Non blocking request response
  def subtract(first: Int, second: Int): Future[Int] = Promise successful first - second

  // fire and forget
  def incrementCount(): Unit = counter += 1

  // Blocking request response
  def incrementAndReturn(): Option[Int] = {
    counter += 1
    Some(counter)
  }

  def onReceive(message: Any, sender: ActorRef): Unit = {
    log.info("Message received-> {}", message)
    childActor.tell(message, sender)
  }

  // Allows to tap into the Actor PreStart hook
  def preStart(): Unit = {
    log.info("Actor Started")
  }

  // Allows to tap into the Actor PostStop hook
  def postStop(): Unit = {
    log.info("Actor Stopped")
  }

  def supervisorStrategy(): SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds) {
    case _: ArithmeticException => Resume
    case _: IllegalArgumentException => Restart
    case _: NullPointerException => Stop
    case _: Exception => Escalate
  }
}

