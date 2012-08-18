package org.akka.essentials.stm.transactor.example2
import scala.concurrent.stm.Ref
import akka.util.duration._
import akka.transactor.Transactor
import akka.actor.SupervisorStrategy._
import akka.actor.OneForOneStrategy
import akka.transactor.CoordinatedTransactionException
import java.lang.Float;

class BalanceTransactor extends Transactor {

  val balance = Ref(Float.valueOf(0))

  def atomically = implicit txn => {
    case message: AccountDebit =>
      if (balance.single.get < message.amount)
        throw new IllegalStateException("Insufficient Balance")
      else
        balance transform (_ - message.amount)
    case message: AccountCredit =>
      balance transform (_ + message.amount)
  }

  override def normally: Receive = {
    case message: String =>
      if ("BALANCE".equals(message))
        sender.tell(balance.single.get)
  }
}