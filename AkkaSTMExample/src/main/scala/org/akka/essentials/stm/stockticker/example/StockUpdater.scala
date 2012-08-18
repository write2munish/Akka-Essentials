package org.akka.essentials.stm.stockticker.example
import akka.agent.Agent

class StockUpdater(stock: Stock) extends Runnable {

  var countDown = 5

  override def run(): Unit = {
    while (countDown > 0) {
      Thread.sleep(75)
      val x: String = Thread.currentThread().getName()
      stock.price.send(_+10)
      println("Quote update by thread (" + x + "), current price " + stock.price.get)
      countDown = countDown - 1
    }
  }
}