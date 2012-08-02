package org.akka.essentials.zeromq.example3.client
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RoundRobinRouter

object MyClientActorSystem {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("zeromqTest")
    system.actorOf(Props[ClientActor].withRouter(
      RoundRobinRouter(nrOfInstances = 3)), name = "client")
  }
}