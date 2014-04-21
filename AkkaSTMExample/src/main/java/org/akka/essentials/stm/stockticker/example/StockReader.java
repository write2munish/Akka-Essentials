package org.akka.essentials.stm.stockticker.example;

public class StockReader implements Runnable {

	private int countDown = 10;
	private Stock stock;

	public StockReader(Stock inStock) {
		stock = inStock;
	}

	public void run() {
		while (countDown > 0) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			String x = Thread.currentThread().getName();
			Float stockTicker = stock.getPrice().get();
			System.out.println("Quote read by thread (" + x
					+ "), current price " + stockTicker);
			countDown = countDown - 1;
		}
	}

}
