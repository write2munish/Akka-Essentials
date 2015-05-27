package org.akka.essentials.data;

import java.io.Serializable;

public class OperationCmd implements Serializable {

	private static final long serialVersionUID = -4380291231130584842L;

	private final CalculationEvent event;

	public OperationCmd(Operator op, Integer num) {
		this.event = new CalculationEvent(op, num);

	}

	public CalculationEvent getCalculationEvent() {
		return event;
	}
}
