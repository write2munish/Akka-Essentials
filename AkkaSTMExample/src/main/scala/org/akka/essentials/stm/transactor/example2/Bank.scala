package org.akka.essentials.stm.transactor.example2
import akka.actor.ActorSystem
import akka.actor.Props
import akka.dispatch.Future
import akka.pattern.ask
import akka.util.Timeout
import akka.util.duration._
import akka.dispatch.Await
import java.lang.Float

case class AccountBalance(accountNumber: String, accountBalance: Float)
case class AccountCredit(amount: Float)
case class AccountDebit(amount: Float)
case class TransferMsg(amtToBeTransferred: Float)

object Bank {

  def main(args: Array[String]): Unit = {}
  val system = ActorSystem("STM-Example")
  implicit val timeout = Timeout(5 seconds)

  val transfer = system.actorOf(Props[TransferActor], name = "transferActor")

  transfer.tell(new TransferMsg(1500));

  showBalances();

  transfer.tell(new TransferMsg(1400));

  showBalances();

  transfer.tell(new TransferMsg(3500));

  showBalances();

  system.shutdown()

  def showBalances(): Unit = {
    
    Thread.sleep(2000)
    
    var future = transfer ? new AccountBalance("XYZ", Float.parseFloat("0"))

    var balance = Await.result(future, timeout.duration).asInstanceOf[AccountBalance]

    System.out.println("Account #" + balance.accountNumber
      + " , Balance " + balance.accountBalance);

    future = transfer ? new AccountBalance("ABC", Float.parseFloat("0"))

    balance = Await.result(future, timeout.duration).asInstanceOf[AccountBalance]

    System.out.println("Account #" + balance.accountNumber
      + " , Balance " + balance.accountBalance);
  }

}