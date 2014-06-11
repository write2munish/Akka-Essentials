package org.akka.essentials.scala.router.custom.example

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.SupervisorStrategy
import akka.dispatch.Dispatchers
import akka.routing.Destination
import akka.routing.Route
import akka.routing.RouteeProvider
import akka.routing.RouterConfig

class BurstyMessageRouter(noOfInstances: Int, messageBurst: Int) extends RouterConfig {
  var messageCount = 0
  var actorSeq = 0
  def routerDispatcher: String = Dispatchers.DefaultDispatcherId
  def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.defaultStrategy

  def createRoute(routeeProvider: RouteeProvider): Route = {

    val routees = new Array[ActorRef](noOfInstances)
    for (i <- 0 until noOfInstances){
      routees.update(i,routeeProvider.context.actorOf(Props(new MsgEchoActor())))
    } 
    routeeProvider.registerRoutees(routees)

    {
      case (sender, message) =>
        var actor = routeeProvider.routees(actorSeq)
        //increment message count
        synchronized { messageCount += 1 }
        //check message count
        if (messageCount == messageBurst) {
          actorSeq += 1
          //reset the counter
          synchronized { messageCount = 0 }
          //reset actorseq counter
          if (actorSeq == noOfInstances) {
            actorSeq = 0
          }
        }
        List(Destination(sender, actor))
    }
  }
}