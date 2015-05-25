package org.akka.essentials.data;

import java.io.Serializable;

public class Operation implements Serializable {

	private static final long serialVersionUID = -4380291231130584842L;

	private final Operator op;
	private final Integer number;

	public Operation(Operator op, Integer num) {
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
