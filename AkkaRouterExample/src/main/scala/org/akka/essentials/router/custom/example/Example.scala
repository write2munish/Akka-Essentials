package org.akka.essentials.router.custom.example
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RandomRouter

object Example {

  def main(args: Array[String]): Unit = {

    val _system = ActorSystem("CustomRouterExample")
    val burstyMessageRouter = _system.actorOf(Props[MsgEchoActor].withRouter(new BurstyMessageRouter(5, 2)))
    1 to 13 foreach {
      i => burstyMessageRouter ! i
    }
    _system.shutdown()
  }
}