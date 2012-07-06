package org.akka.essentials.hotswap;

import akka.actor.UntypedActor;
import akka.japi.Procedure;

public class CalculatorActor extends UntypedActor {

	static final String ADD = "ADD";
	static final String SUBTRACT = "SUBTRACT";
	static final String MULTIPLY = "MULTIPLY";
	static final String DIVIDE = "DIVIDE";

	
	@Override
	public void preStart() {
		getContext().become(add);
	}

	@Override
	public void onReceive(Object message) throws Exception {

	}

	Procedure<Object> add = new Procedure<Object>() {
		public void apply(Object message) {
			if (message instanceof Numbers) {
				Numbers number = (Numbers) message;
				getSender().tell(number.first + number.second);
			} else if (message instanceof String) {
				checkAndUpdate((String) message);
			}
		}
	};

	Procedure<Object> subtract = new Procedure<Object>() {
		public void apply(Object message) {
			if (message instanceof Numbers) {
				Numbers number = (Numbers) message;
				getSender().tell(number.first - number.second);
			} else if (message instanceof String) {
				checkAndUpdate((String) message);
			}
		}
	};
	
	Procedure<Object> multiply = new Procedure<Object>() {
		public void apply(Object message) {
			if (message instanceof Numbers) {
				Numbers number = (Numbers) message;
				getSender().tell(number.first * number.second);
			} else if (message instanceof String) {
				checkAndUpdate((String) message);
			}
		}
	};
	
	Procedure<Object> divide = new Procedure<Object>() {
		public void apply(Object message) {
			if (message instanceof Numbers) {
				Numbers number = (Numbers) message;
				getSender().tell(number.first / number.second);
			} else if (message instanceof String) {
				checkAndUpdate((String) message);
			}
		}
	};

	private void checkAndUpdate(String msg) {
		if (msg.equals(ADD)) {
			getContext().unbecome();
			getContext().become(add);
		} else if (msg.equals(SUBTRACT)) {
			getContext().unbecome();
			getContext().become(subtract);
		} else if (msg.equals(MULTIPLY)) {
			getContext().unbecome();
			getContext().become(multiply);
		} else if (msg.equals(DIVIDE)) {
			getContext().unbecome();
			getContext().become(divide);
		}
	}
}
