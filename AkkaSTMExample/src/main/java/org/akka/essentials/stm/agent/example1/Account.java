package org.akka.essentials.stm.agent.example1;

import akka.actor.UntypedActor;
import akka.agent.Agent;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.transactor.Coordinated;

public class Account extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	String accountNumber;

	Agent<Float> balance = new Agent<Float>(Float.valueOf("0"), getContext().system());

	public Account(String accNo, Float bal) {
		this.accountNumber = accNo;
		balance.send(bal);
	}

	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof Coordinated) {
			Coordinated coordinated = (Coordinated) o;
			final Object message = coordinated.getMessage();
			if (message instanceof AccountDebit) {
				coordinated.atomic(new Runnable() {
					public void run() {
						AccountDebit accDebit = (AccountDebit) message;
						// check for funds availability
						if (balance.get() > accDebit.getAmount()) {
							balance.send(balance.get() - accDebit.getAmount());
						} else {
							throw new IllegalStateException(
									"Insufficient Balance");

						}
					}
				});

			} else if (message instanceof AccountCredit) {
				coordinated.atomic(new Runnable() {
					public void run() {
						AccountCredit accCredit = (AccountCredit) message;
						balance.send(balance.get() + accCredit.getAmount());
					}
				});
			}
		} else if (o instanceof AccountBalance) {
			// reply with the account balance
			sender().tell(new AccountBalance(accountNumber, balance.get()));
		} else
			unhandled(o);
	}
}
