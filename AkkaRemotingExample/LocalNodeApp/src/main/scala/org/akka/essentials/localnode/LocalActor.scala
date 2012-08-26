package org.akka.essentials.localnode
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Address
import akka.actor.Deploy
import akka.actor.Props
import akka.dispatch.Await
import akka.pattern.ask
import akka.remote.RemoteScope
import akka.util.duration.intToDurationInt
import akka.util.Timeout

class LocalActor extends Actor with ActorLogging {

  //Get a reference to the remote actor
  val remoteActor = context.actorFor("akka://RemoteNodeApp@10.101.161.20:2552/user/remoteActor")
  implicit val timeout = Timeout(5 seconds)
  def receive: Receive = {
    case message: String =>
      val future = (remoteActor ? message).mapTo[String]
      val result = Await.result(future, timeout.duration)
      log.info("Message received from Server -> {}", result)
  }
}