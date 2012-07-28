package org.akka.essentials.router.example2.config
import org.akka.essentials.router.example.MsgEchoActor
import akka.actor.actorRef2Scala
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory

object Example {
  def main(args: Array[String]): Unit = {
    val _system = ActorSystem.create("RandomRouterExample", ConfigFactory.load()
      .getConfig("MyRouterExample"))
    val randomRouter = _system.actorOf(Props[MsgEchoActor].withRouter(FromConfig()), name = "myRandomRouterActor")
    1 to 10 foreach {
      i => randomRouter ! i
    }
    _system.shutdown()
  }
}