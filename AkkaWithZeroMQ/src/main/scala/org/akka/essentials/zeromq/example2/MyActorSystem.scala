package org.akka.essentials.zeromq.example2
import akka.dispatch.Await
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.actor.Props

object MyActorSystem {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("zeromqTest")
    system.actorOf(Props[RouterActor], name = "router")
    system.actorOf(Props[WorkerTaskA], name = "workerA")
    system.actorOf(Props[WorkerTaskB], name = "workerB")
  }
}