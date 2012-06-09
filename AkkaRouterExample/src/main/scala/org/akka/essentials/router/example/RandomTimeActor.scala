package org.akka.essentials.router.example
import akka.actor.Actor
import java.util.Random
import java.util.concurrent.TimeUnit

class RandomTimeActor extends Actor {
	val randomGenerator = new Random()
	def receive: Receive = {
		case message =>
			val sleepTime = randomGenerator.nextInt(5)
			System.out.println("Actor # " + self.path.name + " will return in " + sleepTime)
			TimeUnit.SECONDS.sleep(sleepTime);
			sender.tell("Message from Actor #" + self.path)
	}
}