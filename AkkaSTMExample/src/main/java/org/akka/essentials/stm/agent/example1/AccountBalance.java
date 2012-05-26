package org.akka.essentials.stm.agent.example1;

public class AccountBalance {

	String accountNumber = "";
	Float accountBalance = Float.valueOf("0");

	public AccountBalance(String no) {
		accountNumber = no;
	}

	public AccountBalance(String no, Float bal) {
		accountNumber = no;
		accountBalance = bal;
	}

	public Float getBalance() {
		return accountBalance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
}
