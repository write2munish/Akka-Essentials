package org.akka.essentials.grid.worker
import org.akka.essentials.grid.controller.ENOUGH
import org.akka.essentials.grid.controller.STOP
import org.akka.essentials.grid.controller.Task
import org.akka.essentials.grid.controller.TaskFinished

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorLogging

/*
 * the message processed counter gets reset when the grid controller starts and stops 
 * the worker instance #bug
 * 
 */
class WorkerActor extends Actor with ActorLogging {
  var instanceCounter: Int = 0
  var messageProcessedCounter: Int = 0

  override def preStart() {
    instanceCounter += 1
    log.info("Starting WorkerActor instance #" + instanceCounter
      + ", hashcode #" + this.hashCode())
  }
  override def postStop() {
    log.info("Stopping WorkerActor instance #" + instanceCounter
      + ", hashcode #" + this.hashCode())
    instanceCounter -= 1
  }
  def receive: Receive = {
    case STOP =>
      // used when the new routee gets added to the server
      // we stop the currently running actor's on each client
      context.stop(self)

    case task: Task =>
      // check whether we have processed enough messages
      if (messageProcessedCounter == 10) {
        // tell the server, enough messages for me and shut me down
        sender ! new ENOUGH
        context.stop(self)
        context.system.shutdown()
      } else {
        log.info("Work Packet from server->" + task.taskNumber)
        messageProcessedCounter += 1;
        sender ! new TaskFinished(task.taskNumber)
      }
  }
}