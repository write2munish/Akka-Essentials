package org.akka.essentials.router.example.broadcast
import akka.actor.ActorSystem
import akka.actor.Props
import org.akka.essentials.router.example.MsgEchoActor
import akka.routing.BroadcastRouter

object Example {

  def main(args: Array[String]): Unit = {
    val _system = ActorSystem("BroadcastRouterExample")
    val broadcastRouter = _system.actorOf(Props[MsgEchoActor].withRouter(BroadcastRouter(5)), name = "myBroadcastRouterActor")
    1 to 1 foreach {
      i => broadcastRouter ! i
    }
    _system.shutdown()
  }

}