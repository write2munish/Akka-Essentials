package org.akka.essentials.router.example.scattergatherfirstcomplete
import org.akka.essentials.router.example.MsgEchoActor
import org.akka.essentials.router.example.RandomTimeActor

import akka.actor.ActorSystem
import akka.actor.Props
import akka.dispatch.Await
import akka.pattern.ask
import akka.routing.ScatterGatherFirstCompletedRouter
import akka.util.duration._
import akka.util.Timeout

object Example {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem("SGFCRouterExample")
    val scatterGatherFirstCompletedRouter = _system.actorOf(Props[RandomTimeActor].withRouter(
      ScatterGatherFirstCompletedRouter(nrOfInstances = 5, within = 5 seconds)), name = "mySGFCRouterActor")

    implicit val timeout = Timeout(5 seconds)
    val futureResult = scatterGatherFirstCompletedRouter ? "message"
    val result = Await.result(futureResult, timeout.duration)
    System.out.println(result)

    _system.shutdown()
  }
}