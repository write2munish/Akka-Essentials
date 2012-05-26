package org.akka.essentials.stm.agent.example1;

public abstract interface AccountMsg {

	public void setAmount(Float bal);

	public Float getAmount();
}
