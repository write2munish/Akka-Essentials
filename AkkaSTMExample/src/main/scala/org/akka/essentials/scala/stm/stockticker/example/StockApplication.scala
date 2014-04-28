package org.akka.essentials.scala.stm.stockticker.example

import akka.actor.ActorSystem
import akka.agent.Agent
import java.lang.Float

case class Stock(symbol: String, price: Agent[Float])

object StockApplication {

  def main(args: Array[String]): Unit = {
    val _system = ActorSystem("Agent-example")
    val stock = new Stock("APPL", Agent(new Float("600.45"))(_system))

    val readerThreads = new Array[Thread](10)
    val updateThreads = new Array[Thread](10)

    for (i <- 0 until readerThreads.length) {
      readerThreads(i) = new Thread(new StockReader(stock))
      readerThreads(i).setName("#" + i)
    }
    for (i <- 0 until updateThreads.length) {
      updateThreads(i) = new Thread(new StockUpdater(stock))
      updateThreads(i).setName("#" + i)
    }

    for (i <- 0 until readerThreads.length)
      readerThreads(i).start()

    for (i <- 0 until updateThreads.length)
      updateThreads(i).start()

    Thread.sleep(3000)
    _system.shutdown()
  }
}