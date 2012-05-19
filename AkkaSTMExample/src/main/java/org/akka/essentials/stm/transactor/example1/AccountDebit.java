package org.akka.essentials.stm.transactor.example1;

public class AccountDebit implements AccountMsg {
	Float amount =  Float.parseFloat("0");

	public AccountDebit(Float amt) {
		amount = amt;
	}

	public void setAmount(Float bal) {
		amount = bal;

	}

	public Float getAmount() {
		return amount;
	}

}
