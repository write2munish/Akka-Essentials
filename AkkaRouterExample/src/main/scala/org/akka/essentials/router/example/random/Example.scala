package org.akka.essentials.router.example.random
import akka.actor.ActorSystem
import akka.actor.Props
import org.akka.essentials.router.example.MsgEchoActor
import akka.routing.RandomRouter

object Example {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem("RandomRouterExample")
    val randomRouter = _system.actorOf(Props[MsgEchoActor].withRouter(RandomRouter(5)), name = "myRandomRouterActor")
    1 to 10 foreach {
      i => randomRouter ! i
    }
    _system.shutdown()
  }
}