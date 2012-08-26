package org.akka.essentials.remotenode
import akka.kernel.Bootable
import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory

class RemoteNodeApplication extends Bootable {
  val system = ActorSystem("RemoteNodeApp", ConfigFactory
    .load().getConfig("RemoteSys"))

  def startup = {
    system.actorOf(Props[RemoteActor], name = "remoteActor")
  }

  def shutdown = {
    system.shutdown()
  }
}