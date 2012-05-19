package org.akka.essentials.stm.transactor.example1;

public abstract interface AccountMsg {

	public void setAmount(Float bal);

	public Float getAmount();
}
