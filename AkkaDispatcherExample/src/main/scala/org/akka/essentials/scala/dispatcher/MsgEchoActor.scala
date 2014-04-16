package org.akka.essentials.scala.dispatcher
import akka.actor.Actor

class MsgEchoActor extends Actor {
  @volatile var messageProcessed:Int = 0

	def receive: Receive = {
		case message =>
			messageProcessed = messageProcessed + 1
      Thread.sleep(100)
			println(
				"Received Message %s in Actor %s using Thread %s, total message processed %s".format( message,
				self.path.name, Thread.currentThread().getName(), messageProcessed))
	}
}