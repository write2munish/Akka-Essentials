package org.akka.essentials.stm.transactor.example2;

import static akka.pattern.Patterns.ask;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Await;
import akka.kernel.Bootable;
import akka.util.Duration;

public class Bank implements Bootable {
	ActorSystem _system;
	ActorRef transfer;

	public Bank() {
		_system = ActorSystem.apply("STM-Example");

		transfer = _system.actorOf(new Props(TransferActor.class));

		transfer.tell(new TransferMsg(Float.valueOf("1500")));

		showBalances();

		transfer.tell(new TransferMsg(Float.valueOf("1400")));

		showBalances();

		transfer.tell(new TransferMsg(Float.valueOf("3500")));

		showBalances();

		transfer.tell(new AccountDebit(Float.valueOf("1000")));
		transfer.tell(new AccountCredit(Float.valueOf("1770")));

		showBalances();

		_system.shutdown();
	}

	public static void main(String args[]) {
		new Bank();

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	private void showBalances() {
		try {
			Thread.sleep(2000);
			AccountBalance balance = (AccountBalance) Await.result(
					ask(transfer, new AccountBalance("XYZ"), 5000),
					Duration.parse("5 second"));

			System.out.println("Account #" + balance.accountNumber
					+ " , Balance " + balance.accountBalance);

			balance = (AccountBalance) Await.result(
					ask(transfer, new AccountBalance("ABC"), 5000),
					Duration.parse("5 second"));

			System.out.println("Account #" + balance.accountNumber
					+ " , Balance " + balance.accountBalance);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
