package org.akka.essentials.stm.transactor.example;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import java.util.concurrent.TimeUnit;

import org.akka.essentials.stm.transactor.example.msg.AccountBalance;
import org.akka.essentials.stm.transactor.example.msg.AccountCredit;
import org.akka.essentials.stm.transactor.example.msg.AccountDebit;
import org.akka.essentials.stm.transactor.example.msg.TransferMsg;

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
			return new AccountActor(fromAccount, Float.parseFloat("5000"));
		}
	}), fromAccount);
	// sets the to account with initial balance of 1000
	ActorRef to = context().actorOf(new Props(new UntypedActorFactory() {
		public UntypedActor create() {
			return new AccountActor(toAccount, Float.parseFloat("1000"));
		}
	}), toAccount);
	Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
	
	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof TransferMsg) {
			final TransferMsg transfer = (TransferMsg) message;		
			final Coordinated coordinated = new Coordinated(timeout);
			coordinated.atomic(new Runnable() {
				public void run() {
					// credit amount - will always be successful
					to.tell(coordinated.coordinate(new AccountCredit(transfer
							.getAmtToBeTransferred())));
					// debit amount - throws an exception if funds insufficient
					from.tell(coordinated.coordinate(new AccountDebit(transfer
							.getAmtToBeTransferred())));
				}
			});
		} else if (message instanceof AccountBalance) {

			AccountBalance accBalance = (AccountBalance) message;
			// check the account number and return the balance
			if (accBalance.getAccountNumber().equals(fromAccount)) {
				from.tell(accBalance, sender());
			}
			if (accBalance.getAccountNumber().equals(toAccount)) {
				to.tell(accBalance, sender());
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
