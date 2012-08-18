package org.akka.essentials.stm.transactor.example2;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;
import static akka.pattern.Patterns.ask;

import org.akka.essentials.stm.transactor.example2.msg.AccountBalance;
import org.akka.essentials.stm.transactor.example2.msg.AccountCredit;
import org.akka.essentials.stm.transactor.example2.msg.AccountMsg;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.dispatch.Await;
import akka.japi.Function;
import akka.transactor.Coordinated;
import akka.transactor.CoordinatedTransactionException;
import akka.util.Duration;

public class AccountActor extends UntypedActor {

	String accountNumber;
	ActorRef balance = context().actorOf(new Props(BalanceTransactor.class),"BalanceActor");

	public AccountActor(String accNo, Float bal) {
		this.accountNumber = accNo;
		balance.tell(new AccountCredit(bal));
	}

	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof Coordinated) {
			// pass the message to the untypedtransctor
			balance.tell(o);
		} else if (o instanceof AccountBalance) {
			Float amtbalance = (Float) Await.result(
					ask(balance, "BALANCE", 1000), Duration.parse("1 second"));
			// reply with the account balance
			sender().tell(new AccountBalance(accountNumber, amtbalance));
		} else if (o instanceof AccountMsg) {
			balance.tell(o);
		} else
			unhandled(o);
	}

	// catch the exceptions and apply the right strategy, in this case resume()
	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
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
