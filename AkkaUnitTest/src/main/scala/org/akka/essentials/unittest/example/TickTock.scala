package org.akka.essentials.unittest.example
import akka.actor.Actor

case class Tick
case class Tock

class TickTock extends Actor {

  var state = false;

  def receive: Receive = {
    case message: Tick => tick_this(message)
    case message: Tock => tock_this(message);
    case _ => unhandled()
  }

   def tock_this(message: Object) = {
    // do some processing here
    if (state == false)
      state = true;
    else
      state = false;
  }

  def tick_this(message: Object) = {
    // do some processing here
    sender.tell("processed the tick message");
  }
}