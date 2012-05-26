package org.akka.essentials.stm.agent.example1;

public class AccountCredit implements AccountMsg {
	Float amount = Float.valueOf("0");

	public AccountCredit(Float amt) {
		amount = amt;
	}

	public void setAmount(Float bal) {
		amount = bal;

	}

	public Float getAmount() {
		return amount;
	}

}
