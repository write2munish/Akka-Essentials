package org.akka.essentials.calculator
import akka.dispatch.Future
import akka.dispatch.Promise
import akka.actor.TypedActor
import akka.actor.ActorRef
import akka.actor.SupervisorStrategy
import akka.actor.OneForOneStrategy
import akka.util.duration._
import akka.actor.SupervisorStrategy._
import akka.actor.ActorLogging
import akka.event.Logging
import akka.actor.TypedActor.PreStart
import akka.actor.TypedActor.Supervisor
import akka.actor.TypedActor.PostStop

class Calculator extends CalculatorInt with PreStart with PostStop {

  import TypedActor.context
  var counter: Int = 0
  val log = Logging(context.system, TypedActor.self.getClass())

  import TypedActor.dispatcher

  //Non blocking request response
  def add(first: Int, second: Int): Future[Int] = Promise successful first + second
  //Non blocking request response
  def subtract(first: Int, second: Int): Future[Int] = Promise successful first - second
  //fire and forget
  def incrementCount(): Unit = counter += 1
  //Blocking request response
  def incrementAndReturn(): Option[Int] = {
    counter += 1
    Some(counter)
  }

  def onReceive(message: Any, sender: ActorRef): Unit = {
    log.info("Message received-> {}", message)
  }

  //Allows to tap into the Actor PreStart hook
  def preStart(): Unit = {
    log.info("Actor Started")
  }
  //Allows to tap into the Actor PostStop hook
  def postStop(): Unit = {
    log.info("Actor Stopped")
  }

}

