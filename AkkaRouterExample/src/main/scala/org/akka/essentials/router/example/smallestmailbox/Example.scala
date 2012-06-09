package org.akka.essentials.router.example.smallestmailbox
import akka.actor.ActorSystem
import akka.actor.Props
import org.akka.essentials.router.example.MsgEchoActor
import akka.routing.RandomRouter
import akka.routing.SmallestMailboxRouter

object Example {
	def main(args: Array[String]): Unit = {}
	val _system = ActorSystem.create("SmallestMailBoxRouterExample")
	val smallestMailBoxRouter = _system.actorOf(Props[MsgEchoActor].withRouter(SmallestMailboxRouter(5)), name = "mySmallestMailBoxRouterActor")
	1 to 10 foreach {
		i => smallestMailBoxRouter ! i
	}
	_system.shutdown()
}