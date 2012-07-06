package org.akka.essentials.calculator;

import akka.dispatch.Future;
import akka.japi.Option;

public interface CalculatorInt {

	public Future<Integer> add(Integer first, Integer second);

	public Future<Integer> subtract(Integer first, Integer second);

	public void incrementCount();

	public Option<Integer> incrementAndReturn();

}
