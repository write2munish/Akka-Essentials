package org.akka.essentials.router.example2.custom
import org.akka.essentials.router.example.MsgEchoActor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef
import akka.routing.FromConfig
import akka.routing.RoundRobinRouter
import akka.routing.RandomRouter

object Example1 {

  def main(args: Array[String]): Unit = {
    val _system = ActorSystem.create("CustomRouteeRouterExample")

    val echoActor1 = _system.actorOf(Props[MsgEchoActor])
    val echoActor2 = _system.actorOf(Props[MsgEchoActor])
    val echoActor3 = _system.actorOf(Props[MsgEchoActor])

    val routees = Vector[ActorRef](echoActor1, echoActor2, echoActor3)

    val randomRouter = _system.actorOf(Props[MsgEchoActor].withRouter(RandomRouter(routees = routees)))
    1 to 10 foreach {
      i => randomRouter ! i
    }
    _system.shutdown()
  }
}