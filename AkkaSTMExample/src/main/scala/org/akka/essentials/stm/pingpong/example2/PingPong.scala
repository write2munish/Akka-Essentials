package org.akka.essentials.stm.pingpong.example2

import scala.concurrent.stm.Ref

class PingPong(whoseTurn: Ref[String]) {

  def hit(opponent: String): Boolean = {

    val x: String = Thread.currentThread().getName()

    if (whoseTurn.single.get == "") {
      whoseTurn.single.set(x)
      return true
    } else if (whoseTurn.single.get.compareTo(x) == 0) {
      println("PING! (" + x + ")")
      whoseTurn.single.set(opponent)
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
      whoseTurn.single.set(opponent)
      return false
    }
    if (whoseTurn.single.get.compareTo("DONE") == 0){
      return false
    }
    return true // keep playing.
  }
}