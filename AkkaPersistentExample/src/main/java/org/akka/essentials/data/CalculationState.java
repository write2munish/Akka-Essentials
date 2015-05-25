package org.akka.essentials.data;

import java.io.Serializable;
import java.util.HashMap;

public class CalculationState implements Serializable {
	private static final long serialVersionUID = 1L;
	private final HashMap<Operator, Integer> operations;

	public CalculationState() {
		this(new HashMap<Operator, Integer>());
	}

	public CalculationState(HashMap<Operator, Integer> ops) {
		this.operations = ops;
	}

	public CalculationState copy() {
		return new CalculationState(new HashMap<Operator,Integer>(operations));
	}
	
	public void update(Operator op, Integer num) {
		operations.put(op, num);
	}

	public int size() {
		return operations.size();
	}

	@Override
	public String toString() {
		return operations.toString();
	}
}
