package org.akka.essentials.supervisor.example3
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorLogging

class WorkerActor extends Actor with ActorLogging {
  import org.akka.essentials.supervisor.example1.Result
  var state: Int = 0;

  override def preStart() {
    log.info("Starting WorkerActor instance hashcode #" + this.hashCode());
  }
  override def postStop() {
    log.info("Stopping WorkerActor instance hashcode #" + this.hashCode());
  }
  def receive: Receive = {
    case value: Int =>
        state = value;
    case result: Result =>
      sender ! state
    case _ =>
      context.stop(self)
  }
}