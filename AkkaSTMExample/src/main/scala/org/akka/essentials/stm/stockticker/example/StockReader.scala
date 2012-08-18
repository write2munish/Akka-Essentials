package org.akka.essentials.stm.stockticker.example
import akka.agent.Agent

class StockReader(stock: Stock) extends Runnable {

  var countDown = 10

  override def run(): Unit = {
    while (countDown > 0) {
      Thread.sleep(50)
      val x: String = Thread.currentThread().getName()
      val stockTicker = stock.price.get
      println("Quote read by thread (" + x + "), current price " + stockTicker)
      countDown = countDown - 1
    }
  }
}