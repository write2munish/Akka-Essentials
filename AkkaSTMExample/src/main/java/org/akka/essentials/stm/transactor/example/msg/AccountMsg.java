package org.akka.essentials.stm.transactor.example.msg;

public abstract interface AccountMsg {

	public void setAmount(Float bal);

	public Float getAmount();
}
