package org.akka.essentials.unittest.example
import akka.actor.Actor

case class Tick
case class Tock

class TickTock extends Actor {

  var state = false;

  def receive: Receive = {
    case message: Tick => tick_this(message)
    case message: Tock => tock_this(message);
  }

   def tock_this(message: Tock) = {
    // do some processing here
    if (state == false)
      state = true;
    else
      state = false;
  }

  def tick_this(message: Tick) = {
    // do some processing here
    sender.tell("processed the tick message");
  }
}