package org.akka.essentials.router.example
import akka.actor.Actor
import java.util.Random
import java.util.concurrent.TimeUnit
import akka.actor.ActorLogging

class RandomTimeActor extends Actor with ActorLogging{
  val randomGenerator = new Random()
  def receive: Receive = {
    case message =>
      val sleepTime = randomGenerator.nextInt(5)
      log.info("Actor # {} will return in {}", self.path.name, sleepTime)
      TimeUnit.SECONDS.sleep(sleepTime);
      sender.tell("Message from Actor #" + self.path)
  }
}