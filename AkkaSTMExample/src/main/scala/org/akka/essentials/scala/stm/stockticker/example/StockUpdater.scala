package org.akka.essentials.scala.stm.stockticker.example


class StockUpdater(stock: Stock) extends Runnable {

  var countDown = 5

  override def run(): Unit = {
    while (countDown > 0) {
      Thread.sleep(55)
      val x: String = Thread.currentThread().getName
      stock.price.send(_ + 10)
      println("Quote update by thread (" + x + "), current price " + stock.price.get)
      countDown = countDown - 1
    }
  }
}