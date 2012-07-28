package org.akka.essentials.router.example2.custom
import akka.routing.DefaultResizer
import akka.actor.ActorSystem
import akka.actor.Props
import org.akka.essentials.router.example.MsgEchoActor
import akka.routing.RandomRouter

object Example3 {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem.create("CustomRouteeRouterExample")

    val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)

    val randomRouter = _system.actorOf(Props[MsgEchoActor].withRouter(RandomRouter(resizer = Some(resizer))))
    1 to 10 foreach {
      i => randomRouter ! i
    }
    _system.shutdown()
  }
}	