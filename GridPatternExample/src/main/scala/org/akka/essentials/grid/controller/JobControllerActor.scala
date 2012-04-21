package org.akka.essentials.grid.controller
import scala.collection.immutable.HashMap

import org.akka.essentials.grid.controller.StartWorker
import org.akka.essentials.grid.controller.StopWorker
import org.akka.essentials.grid.controller.TaskFinished
import org.akka.essentials.grid.worker.WorkerActor

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Address
import akka.actor.AddressFromURIString
import akka.actor.Props
import akka.routing.RemoteRouterConfig
import akka.routing.RoundRobinRouter

class JobControllerActor(val workSchedulerActor: ActorRef) extends Actor with ActorLogging {

  var workerRouterActor: ActorRef = null
  var count: Int = 0
  var workerAddressMap = new HashMap[String, Address]

  def receive: Receive = {
    case message: StartWorker =>
      log.info("Received worker registration request")
      addWorkerRoute(message.actorPath)
      log.info("worker router updated for the new address")
      workSchedulerActor.tell("You can start sending work!", context.self)
    case message: TaskFinished =>
      log.info("Task finished->" + message.taskNumber);
    case stopMe: StopWorker =>
      log.info("Worker requesting shutdown->" + stopMe.actorPath);
      removeWorkerRoute(stopMe.actorPath);
    case SendWork =>

      log.info("About to send work packets to workers");
      if (workerRouterActor != null) {
        for (i <- 0 until workerAddressMap.size) {
          count += 1
          workerRouterActor.tell(new Task(count), self);

        }
        log.info("Work Packets send upto->" + count);
        // let the workscheduler know that you finished with sending
        // work packets
        workSchedulerActor.tell("Work Packets send", self)

      } else {
        log.info("No workers registered as of now!");
        // tell the workscheduler actor to wake me up in 5 millisecs
        // again
        workSchedulerActor.tell(5000, self)
      }

    case ENOUGH =>
      log.info("Worker requesting shutdown"
        + sender.path.toString())
      // Shut down the routee at this point and remote the
      // routerconfig
      removeWorkerRoute(sender.path.toString())

  }
  def removeWorkerRoute(address: String) = {
    log.info("Processing Worker shutdown request->" + address);

    if (workerRouterActor != null) {
      for (i <- 0 until workerAddressMap.size)
        if (!workerRouterActor.isTerminated)
          workerRouterActor ! STOP
    }
    if (workerAddressMap.size > 0)
      workerAddressMap -= address;

    if (workerAddressMap.size > 0) {
      var workerAddress = workerAddressMap.values;
      workerRouterActor = context.system.actorOf(Props[WorkerActor].withRouter(RemoteRouterConfig(RoundRobinRouter(workerAddressMap.size), workerAddress)))
    } else
      workerRouterActor = null;

  }
  def addWorkerRoute(address: String) = {
    if (workerRouterActor != null) {
      for (i <- 0 until workerAddressMap.size)
        workerRouterActor ! STOP
    }

    workerAddressMap += address -> AddressFromURIString.parse(address);
    var workerAddress = workerAddressMap.values;

    workerRouterActor = context.system.actorOf(Props[WorkerActor].withRouter(RemoteRouterConfig(RoundRobinRouter(workerAddressMap.size), workerAddress)))
  }
  override def preStart() {
    log.info("Starting JobControllerActor instance with hashcode #"
      + this.hashCode());
  }
  override def postStop() {
    log.info("Stop JobControllerActor instance with hashcode #"
      + this.hashCode());
  }
}