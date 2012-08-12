package org.akka.essentials.stm.pingpong.example2

import scala.concurrent.stm.Ref

import akka.actor.ActorSystem

object Game {

  def main(args: Array[String]): Unit = {

    val _system = ActorSystem("Ref-example")
    val turn = Ref(new String())
    val table = new PingPong(turn)

    val alice = new Thread(new Player("bob", table))
    val bob = new Thread(new Player("alice", table))

    alice.setName("alice")
    bob.setName("bob")
    alice.start // alice starts playing
    bob.start // bob starts playing
    try {
      // Wait .5 seconds
      Thread.sleep(500)
    } catch {
      case _ =>
      // eat the exception
    }
    table.hit("DONE") // cause the players to quit their threads.
    try {
      Thread.sleep(1000)
    } catch {
      case _ =>
      // eat the exception
    }
    _system.shutdown
  }

}