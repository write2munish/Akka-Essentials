package org.akka.essentials.stm.transactor.example;

import static akka.pattern.Patterns.ask;

import org.akka.essentials.stm.transactor.example.msg.AccountBalance;
import org.akka.essentials.stm.transactor.example.msg.TransferMsg;
import org.junit.Assert;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Await;
import akka.testkit.TestActorRef;
import akka.testkit.TestKit;
import akka.util.Duration;

public class BankTest extends TestKit {
	static ActorSystem _system = ActorSystem.create("STM-Example");

	public BankTest() {
		super(_system);
	}

	@Test
	public void successTest() throws Exception {
		TestActorRef<BankActor> bank = TestActorRef.apply(new Props(
				BankActor.class), _system);

		bank.tell(new TransferMsg(Float.valueOf("1777")));

		AccountBalance balance = (AccountBalance) Await.result(
				ask(bank, new AccountBalance("XYZ"), 5000),
				Duration.parse("5 second"));

		Assert.assertEquals(Float.parseFloat("3223"), balance.getBalance(),
				Float.parseFloat("0"));

		balance = (AccountBalance) Await.result(
				ask(bank, new AccountBalance("ABC"), 5000),
				Duration.parse("5 second"));

		Assert.assertEquals(Float.parseFloat("2777"), balance.getBalance(),
				Float.parseFloat("0"));
	}

	@Test
	public void failureTest() throws Exception {
		TestActorRef<BankActor> bank = TestActorRef.apply(new Props(
				BankActor.class), _system);

		bank.tell(new TransferMsg(Float.valueOf("1500")));

		AccountBalance balance = (AccountBalance) Await.result(
				ask(bank, new AccountBalance("XYZ"), 5000),
				Duration.parse("5 second"));

		Assert.assertEquals(Float.parseFloat("3500"), balance.getBalance(),
				Float.parseFloat("0"));

		balance = (AccountBalance) Await.result(
				ask(bank, new AccountBalance("ABC"), 5000),
				Duration.parse("5 second"));

		Assert.assertEquals(Float.parseFloat("2500"), balance.getBalance(),
				Float.parseFloat("0"));

		bank.tell(new TransferMsg(Float.valueOf("4000")));

		Thread.sleep(2000);
		
		balance = (AccountBalance) Await.result(
				ask(bank, new AccountBalance("XYZ"), 5000),
				Duration.parse("5 second"));

		Assert.assertEquals(Float.parseFloat("3500"), balance.getBalance(),
				Float.parseFloat("0"));

		balance = (AccountBalance) Await.result(
				ask(bank, new AccountBalance("ABC"), 5000),
				Duration.parse("5 second"));

		Assert.assertEquals(Float.parseFloat("2500"), balance.getBalance(),
				Float.parseFloat("0"));
	}

}
