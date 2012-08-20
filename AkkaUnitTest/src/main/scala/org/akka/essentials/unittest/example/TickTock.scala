package org.akka.essentials.unittest.example
import akka.actor.Actor

case class Tick(message: String)
case class Tock(message: String)

class TickTock extends Actor {

  var state = false

  def receive: Receive = {
    case message: Tick => tick(message)
    case message: Tock => tock(message)
    case _ => throw new IllegalArgumentException("boom!")
  }

  def tock(message: Tock) = {
    // do some processing here
    if (state == false)
      state = true
    else
      state = false
  }

  def tick(message: Tick) = {
    // do some processing here
    sender.tell("processed the tick message")
  }
}