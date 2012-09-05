package org.akka.essentials.future.example.messages;

public class OrderHistory {
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	Order order;
	Address address;

	public OrderHistory(Order inOrder, Address inAddress) {
		order = inOrder;
		address = inAddress;
	}
}
