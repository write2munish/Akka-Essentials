package org.akka.essentials.stm.pingpong.example3

import scala.concurrent.stm.Ref
import akka.agent.Agent
import akka.util.Timeout
import akka.util.duration._
import akka.dispatch.Await

class PingPong(whoseTurn: Agent[String]) {
  implicit val timeout = Timeout(5 seconds)
  def hit(opponent: String): Boolean = {

    val x: String = Thread.currentThread().getName()

    //wait till all the messages are processed to make 
    //you get the correct value, as updated to Agents are
    //async
    val result: String = whoseTurn.await

    if (result == "") {
      whoseTurn send x
      return true
    } else if (result.compareTo(x) == 0) {
      println("PING! (" + x + ")")
      whoseTurn send opponent
      return true
    } else {
      try {
        var t1 = System.currentTimeMillis()
        wait(2500)
        if ((System.currentTimeMillis() - t1) > 2500) {
          println("****** TIMEOUT! " + x
            + " is waiting for " + whoseTurn + " to play.")
        }
      } catch {
        case _ =>
        // eat the exception
      }
    }
    if (opponent.compareTo("DONE") == 0) {
      whoseTurn send opponent
      return false
    }
    if (result.compareTo("DONE") == 0)
      return false
    return true // keep playing.
  }
}