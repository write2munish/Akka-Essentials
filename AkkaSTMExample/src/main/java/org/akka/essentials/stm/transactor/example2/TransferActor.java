package org.akka.essentials.stm.transactor.example2;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.AllForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import akka.transactor.Coordinated;
import akka.transactor.CoordinatedTransactionException;
import akka.util.Duration;
import akka.util.Timeout;

public class TransferActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	String fromAccount = "XYZ";
	String toAccount = "ABC";

	// sets the from account with initial balance of 5000
	ActorRef from = context().actorOf(new Props(new UntypedActorFactory() {
		public UntypedActor create() {
			return new Account(fromAccount, Float.parseFloat("5000"));
		}
	}), fromAccount);
	// sets the to account with initial balance of 1000
	ActorRef to = context().actorOf(new Props(new UntypedActorFactory() {
		public UntypedActor create() {
			return new Account(toAccount, Float.parseFloat("1000"));
		}
	}), toAccount);

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof TransferMsg) {
			final TransferMsg transfer = (TransferMsg) message;
			Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
			final Coordinated coordinated = new Coordinated(timeout);
			// add try catch block to prevent the exception being escalated
			// further
			try {
				coordinated.atomic(new Runnable() {
					public void run() {
						// credit amount - will always be successful
						to.tell(coordinated.coordinate(new AccountCredit(
								transfer.getAmtToBeTransferred())));
						// debit amount - throws an exception if funds
						// insufficient
						from.tell(coordinated.coordinate(new AccountDebit(
								transfer.getAmtToBeTransferred())));
					}
				});
			} catch (CoordinatedTransactionException e) {
				// eat the exception

			}
		} else if (message instanceof AccountBalance) {

			AccountBalance accBalance = (AccountBalance) message;
			// check the account number and return the balance
			if (accBalance.getAccountNumber().equals(fromAccount)) {
				from.tell(accBalance, sender());
			}
			if (accBalance.getAccountNumber().equals(toAccount)) {
				to.tell(accBalance, sender());
			}
		} else if (message instanceof AccountMsg) {
			if (message instanceof AccountDebit) {
				from.tell(message);
			} else if (message instanceof AccountCredit) {
				from.tell(message);
			}
		}
	}

	// catch the exceptions and apply the right strategy, in this case resume()
	private static SupervisorStrategy strategy = new AllForOneStrategy(10,
			Duration.parse("10 second"), new Function<Throwable, Directive>() {

				public Directive apply(Throwable t) {
					if (t instanceof CoordinatedTransactionException) {
						return resume();
					} else if (t instanceof IllegalStateException) {
						return resume();
					} else if (t instanceof IllegalArgumentException) {
						return stop();
					} else {
						return escalate();
					}
				}
			});

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

}
