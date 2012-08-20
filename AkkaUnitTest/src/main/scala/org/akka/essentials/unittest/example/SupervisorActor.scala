package org.akka.essentials.unittest.example
import akka.actor.SupervisorStrategy.Escalate
import akka.actor.SupervisorStrategy.Resume
import akka.actor.SupervisorStrategy.Stop
import akka.actor.actorRef2Scala
import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.OneForOneStrategy
import akka.actor.Props
import akka.util.duration.intToDurationInt

class SupervisorActor() extends Actor {

  var childActor: ActorRef = _

  def getChildActor() = childActor

  def receive: Receive = {
    case message: Props =>
      childActor = context.actorOf(message, name = "childActor")
      sender ! childActor
    case message => 
      childActor.tell(message, sender)
  }

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: NullPointerException => Resume
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }
}