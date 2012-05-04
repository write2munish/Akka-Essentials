package org.akka.essentials.supervisor.example3
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.duration._
import akka.dispatch.Await
import akka.util.Timeout
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.OneForOneStrategy
import akka.dispatch.Future
import akka.actor.SupervisorStrategy._
import akka.actor.ActorRef

case class Result
case class DeadWorker
case class RegisterWorker(val worker: ActorRef, val supervisor: ActorRef)

object MyActorSystem extends App {

  val system = ActorSystem("faultTolerance")
  val log = system.log

  val supervisor = system.actorOf(Props[SupervisorActor], name = "supervisor")

  var mesg: Int = 8;
  supervisor ! mesg

  supervisor ! "Do Something"

  Thread.sleep(4000)

  supervisor ! mesg

  system.shutdown();

}