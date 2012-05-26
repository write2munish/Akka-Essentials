package org.akka.essentials.stm.agent.example1
import java.lang.Float
import akka.actor.ActorSystem
import akka.dispatch.Await
import akka.japi.Function
import akka.pattern.ask
import akka.util.duration._
import akka.util.Timeout
import akka.actor.Props

case class AccountBalance(accountNumber: String, accountBalance: Float)
case class AccountCredit(amount: Float)
case class AccountDebit(amount: Float)
case class TransferMsg(amtToBeTransferred: Float)

object Bank {

	def main(args: Array[String]): Unit = {}
	val system = ActorSystem("STM-Example")
	implicit val timeout = Timeout(5 seconds)

	val transfer = system.actorOf(Props[TransferActor], name = "transferActor")

	Thread.sleep(200)

	transfer.tell(new TransferMsg(1500));

	showBalances();

	transfer.tell(new TransferMsg(1400));

	showBalances();

	transfer.tell(new TransferMsg(3500));

	showBalances();

	system.shutdown()

	def showBalances(): Unit = {
		var future = transfer ? new AccountBalance("XYZ", Float.valueOf("0"))
		var balance = Await.result(future, timeout.duration).asInstanceOf[AccountBalance]
		System.out.println("Account #" + balance.accountNumber
			+ " , Balance " + balance.accountBalance)

		future = transfer ? new AccountBalance("ABC", Float.valueOf("0"))
		balance = Await.result(future, timeout.duration).asInstanceOf[AccountBalance]
		System.out.println("Account #" + balance.accountNumber
			+ " , Balance " + balance.accountBalance)
	}

}