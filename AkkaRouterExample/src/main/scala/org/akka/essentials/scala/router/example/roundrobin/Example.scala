package org.akka.essentials.scala.router.example.roundrobin
import org.akka.essentials.scala.router.example.MsgEchoActor


import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RoundRobinRouter
import java.util.concurrent.TimeUnit

object Example {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem("RoundRobinRouterExample")
    val roundRobinRouter = _system.actorOf(Props[MsgEchoActor].withRouter(RoundRobinRouter(5)), name = "myRoundRobinRouterActor")
    1 to 10 foreach {
      i =>
        roundRobinRouter ! i
        if (i == 5) {
          TimeUnit.MILLISECONDS.sleep(100)
          System.out.println("\n")
        }
    }
    _system.shutdown()
  }
}