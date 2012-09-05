package org.akka.essentials.future.example.messages;

import scala.collection.mutable.StringBuilder;

public class Order {

	Integer userId;
	Integer orderNo;
	Float amount;
	Integer noOfItems;

	public Order(Integer inOrderNo, Float inAmount, Integer inNoOfItems) {
		orderNo = inOrderNo;
		amount = inAmount;
		noOfItems = inNoOfItems;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("OrderNo->").append(orderNo).append(" ,Amount->")
				.append(amount).append(" ,NoOfItems->").append(noOfItems);
		return result.toString();
	}

}
