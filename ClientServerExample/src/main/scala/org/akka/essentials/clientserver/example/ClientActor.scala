package org.akka.essentials.clientserver.example

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

class ClientActor extends Actor with ActorLogging {

  /**
   * Use one of the Options to get a reference to server actor
   */
  //First option - Get a reference to the remote actor
  var serverActor = context.actorFor("akka://ServerSys@127.0.0.1:2552/user/serverActor")
  // Second option - create remote Actor instance
  val addr = Address("akka", "ServerSys", "127.0.0.1", 2552)

  serverActor = context.actorOf(Props[ServerActor].withDeploy(Deploy(scope = RemoteScope(addr))))
  //Third option - creating the actor using the actor name 
  //defined via application.conf
  serverActor = context.actorOf(Props[ServerActor], name = "remoteServerActor")

  def receive: Receive = {
    case message: String =>
      implicit val timeout = Timeout(5 seconds)
      val future = (serverActor ? message).mapTo[String]
      val result = Await.result(future, timeout.duration)
      log.info("Message received from Server -> {}", result)
  }
}