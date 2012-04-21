package org.akka.essentials.grid.controller
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Actor

class RegisterRemoteWorkerActor(val jobControllerActor: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case message: StartWorker =>
      log.info("Received Registration Info from " + message.toString());
      jobControllerActor.tell(message);
    case _ =>
      log.error("Wrong message type received");
  }
}