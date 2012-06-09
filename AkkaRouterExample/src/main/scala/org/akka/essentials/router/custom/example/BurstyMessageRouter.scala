package org.akka.essentials.router.custom.example
import akka.actor.SupervisorStrategy
import akka.dispatch.Dispatchers
import akka.routing.RouterConfig
import akka.actor.ActorRef
import akka.actor.Props
import akka.routing.RouteeProvider
import akka.routing.Destination
import akka.routing.CustomRoute
import akka.actor.ActorRefProvider
import akka.actor.ActorCell
import akka.routing.Route

class BurstyMessageRouter(noOfInstances: Int, messageBurst: Int) extends RouterConfig {
	var messageCount = 0
	var actorSeq = 0
	def routerDispatcher: String = Dispatchers.DefaultDispatcherId
	def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.defaultStrategy

	def createRoute(props: Props, routeeProvider: RouteeProvider): Route = {
		routeeProvider.createAndRegisterRoutees(props, noOfInstances, Nil)

		{
			case (sender, message) =>
				var actor = routeeProvider.routees(actorSeq)
				//increment message count
				messageCount += 1
				//check message count
				if (messageCount == messageBurst) {
					actorSeq += 1
					//reset the counter
					messageCount = 0
					//reset actorseq counter
					if (actorSeq == noOfInstances) {
						actorSeq = 0
					}
				}
				List(Destination(sender, actor))
		}
	}
}