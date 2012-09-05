package org.akka.essentials.future.example.messages;

import scala.collection.mutable.StringBuilder;

public class Address {
	Integer userId;
	String fullName;
	String address1;
	String address2;

	public Address(Integer inUserId, String inFullName, String inAddress1,
			String inAddress2) {
		userId = inUserId;
		fullName = inFullName;
		address1 = inAddress1;
		address2 = inAddress2;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("FullName->").append(fullName).append(" ,Address1->")
				.append(address1).append(" ,Address2->").append(address2);
		return result.toString();
	}
}
