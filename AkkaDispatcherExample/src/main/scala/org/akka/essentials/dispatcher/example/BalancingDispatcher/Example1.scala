package org.akka.essentials.dispatcher.example.BalancingDispatcher
import akka.actor.ActorRef
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import org.akka.essentials.dispatcher.MsgEchoActor
import akka.routing.RandomRouter
import akka.routing.RoundRobinRouter

object Example1 {
	def main(args: Array[String]): Unit = {}
	val _system = ActorSystem.create("balancing-dispatcher",ConfigFactory.load().getConfig("MyDispatcherExample"))

	val actor = _system.actorOf(Props[MsgEchoActor].withDispatcher("balancingDispatcher").withRouter(
						RoundRobinRouter(5)))

	0 to 25 foreach {
		i => actor ! i
	}
	_system.shutdown()
}