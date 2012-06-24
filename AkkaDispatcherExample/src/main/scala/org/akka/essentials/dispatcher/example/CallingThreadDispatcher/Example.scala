package org.akka.essentials.dispatcher.example.CallingThreadDispatcher
import org.akka.essentials.dispatcher.MsgEchoActor

import com.typesafe.config.ConfigFactory

import akka.actor.actorRef2Scala
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RoundRobinRouter

object Example {
	def main(args: Array[String]): Unit = {}
	val _system = ActorSystem.create("callingThread-dispatcher",ConfigFactory.load().getConfig("MyDispatcherExample"))

	val actor = _system.actorOf(Props[MsgEchoActor].withDispatcher("CallingThreadDispatcher").withRouter(
						RoundRobinRouter(5)))

	0 to 5 foreach {
		i => actor ! i
	}
	_system.shutdown()
}