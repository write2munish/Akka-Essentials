package org.akka.essentials.data;

import java.io.Serializable;

public class CalculationEvent implements Serializable {

	private static final long serialVersionUID = -8292612144802844338L;
	private final Operator op;
	private final Integer number;

	public CalculationEvent(Operator op, Integer num) {
		this.op = op;
		this.number = num;
	}

	public Operator getOperator() {
		return op;
	}

	public Integer getNumber() {
		return number;
	}

	public String toString() {
		return op.toString() + "," + number;
	}
}
