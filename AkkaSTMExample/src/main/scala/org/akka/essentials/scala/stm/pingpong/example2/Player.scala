package org.akka.essentials.scala.stm.pingpong.example2

class Player(myOpponent: String, myTable: PingPong) extends Runnable {

  override def run(): Unit = {
    while (myTable.hit(myOpponent)) {}
  }
}