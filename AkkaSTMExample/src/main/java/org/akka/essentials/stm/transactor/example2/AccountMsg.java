package org.akka.essentials.stm.transactor.example2;

public abstract interface AccountMsg {

	public void setAmount(Float bal);

	public Float getAmount();
}
