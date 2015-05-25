package org.akka.essentials.actor;

import static java.util.Arrays.asList;

import org.akka.essentials.data.Operation;

import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedPersistentActor;

public class CalculationActor extends UntypedPersistentActor {

	public String persistenceId() {
		return "calculator-1";
	}

	private Integer state = Integer.valueOf(0);

	@Override
	public void onReceiveRecover(Object msg) {
		if (msg instanceof Operation) {
			//apply the operation message(s) when recovering the actor state
			Operation ops = (Operation) msg;
			calculate(ops);
		} else if (msg instanceof SnapshotOffer) {
			//apply the snapshot message to recover from an intermediate state
			state = Integer.valueOf((String) ((SnapshotOffer) msg).snapshot());
		} else {
			unhandled(msg);
		}
	}

	@Override
	public void onReceiveCommand(Object msg) {
		if (msg instanceof Operation) {
			final Operation ops = (Operation) msg;
			//read the message
			persist(asList(ops), new Procedure<Operation>() {
				public void apply(Operation ops) throws Exception {
					//apply the operation on the state
					calculate(ops);
					//save the operation message on the event stream
					getContext().system().eventStream().publish(ops);
				}
			});

		} else if (msg.equals("snap")) {
			//take an intermediate snapshot of the state. In case of recovery, actor can use the 
			//last saved snapshot and apply events post that. 
			saveSnapshot(state.toString());
		} else if (msg.equals("print")) {
			System.out.println(state);
		} else {
			// System.out.println("data corrupt");
			unhandled(msg);
		}

	}

	private void calculate(Operation ops) {
		switch (ops.getOperator()) {
		case ADD:
			state = state + ops.getNumber();
			break;
		case SUBTRACT:
			state = state - ops.getNumber();
			break;
		case MULTIPLY:
			state = state * ops.getNumber();
			break;
		case DIVIDE:
			state = state / ops.getNumber();
			break;
		default:
			break;
		}
	}
}
