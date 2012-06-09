package org.akka.essentials.router.custom.example
import akka.actor.Actor

class MsgEchoActor extends Actor {
	def receive: Receive = {
		case message =>
			System.out.println("Received Message " + message + " in Actor " + self.path.name)
	}
}