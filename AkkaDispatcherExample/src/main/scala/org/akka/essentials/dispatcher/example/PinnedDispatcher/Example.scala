package org.akka.essentials.dispatcher.example.PinnedDispatcher
import org.akka.essentials.dispatcher.MsgEchoActor

import com.typesafe.config.ConfigFactory

import akka.actor.actorRef2Scala
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RoundRobinRouter

object Example {
	def main(args: Array[String]): Unit = {}
	val _system = ActorSystem.create("pinned-dispatcher",ConfigFactory.load().getConfig("MyDispatcherExample"))

	val actor = _system.actorOf(Props[MsgEchoActor].withDispatcher("pinnedDispatcher").withRouter(
						RoundRobinRouter(5)))

	0 to 25 foreach {
		i => actor ! i
	}
	_system.shutdown()
}