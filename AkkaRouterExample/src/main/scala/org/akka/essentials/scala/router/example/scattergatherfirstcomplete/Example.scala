package org.akka.essentials.scala.router.example.scattergatherfirstcomplete
import org.akka.essentials.scala.router.example.MsgEchoActor
import org.akka.essentials.scala.router.example.RandomTimeActor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.ScatterGatherFirstCompletedRouter
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

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