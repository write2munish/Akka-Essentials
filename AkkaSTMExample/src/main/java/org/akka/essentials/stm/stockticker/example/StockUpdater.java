package org.akka.essentials.stm.stockticker.example;

import akka.japi.Function;

public class StockUpdater implements Runnable {

	private int countDown = 5;
	private Stock stock;

	public StockUpdater(Stock inStock) {
		stock = inStock;
	}

	public void run() {
		while (countDown > 0) {
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
			}
			String x = Thread.currentThread().getName();
			stock.getPrice().send(new Function<Float, Float>() {
				public Float apply(Float i) {
					return i + 10;
				}
			});
			System.out.println("Quote update by thread (" + x
					+ "), current price " + stock.getPrice().get());
			countDown = countDown - 1;
		}
	}
}
