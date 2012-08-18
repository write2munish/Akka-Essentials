package org.akka.essentials.stm.stockticker.example;

import akka.agent.Agent;

public class Stock {
	private String symbol;
	private Agent<Float> price;

	public Stock(String inSymbol, Agent<Float> inPrice) {
		symbol = inSymbol;
		price = inPrice;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Agent<Float> getPrice() {
		return price;
	}

	public void setPrice(Agent<Float> price) {
		this.price = price;
	}
}
