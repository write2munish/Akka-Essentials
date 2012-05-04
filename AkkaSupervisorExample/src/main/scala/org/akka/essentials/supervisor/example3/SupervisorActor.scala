package org.akka.essentials.supervisor.example3
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.dispatch.Future
import akka.pattern.ask
import akka.util.Timeout
import akka.dispatch.Await

class SupervisorActor extends Actor with ActorLogging {
  import akka.actor.OneForOneStrategy
  import akka.actor.SupervisorStrategy._
  import akka.util.duration._
  import org.akka.essentials.supervisor.example3._

  var childActor = context.actorOf(Props[WorkerActor], name = "workerActor")
  val monitor = context.system.actorOf(Props[MonitorActor], name = "monitor")

  override def preStart() {
    monitor ! new RegisterWorker(childActor, self)
  }

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds) {

    case _: ArithmeticException => Resume
    case _: NullPointerException => Restart
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }

  def receive = {
    case result: Result =>
      childActor.tell(result, sender)
    case mesg: DeadWorker =>
      log.info("Got a DeadWorker message, restarting the worker")
      childActor = context.actorOf(Props[WorkerActor], name = "workerActor")
    case msg: Object =>
      childActor ! msg
  }
}