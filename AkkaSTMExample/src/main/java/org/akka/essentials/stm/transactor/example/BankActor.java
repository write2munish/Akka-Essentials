package org.akka.essentials.stm.transactor.example;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;
import static akka.pattern.Patterns.ask;

import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;

import org.akka.essentials.stm.transactor.example.msg.AccountBalance;
import org.akka.essentials.stm.transactor.example.msg.TransferMsg;

import scala.concurrent.Await;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;
import akka.transactor.CoordinatedTransactionException;

public class BankActor extends UntypedActor {

	ActorRef transfer = getContext().actorOf(new Props(TransferActor.class),
			"TransferActor");

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof TransferMsg) {
			transfer.tell(message);
		} else if (message instanceof AccountBalance) {
			AccountBalance account = (AccountBalance) Await.result(
					ask(transfer, message, 5000), Duration.create("5 second"));

			System.out.println("Account #" + account.getAccountNumber()
					+ " , Balance " + account.getBalance());
			
			getSender().tell(account);
		}

	}

	// catch the exceptions and apply the right strategy, in this case resume()
	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
			Duration.create("10 seconds") , new Function<Throwable, Directive>() {

				public Directive apply(Throwable t) {
					if (t instanceof CoordinatedTransactionException) {
						return resume();
					} else if (t instanceof IllegalStateException) {
						return stop();
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
