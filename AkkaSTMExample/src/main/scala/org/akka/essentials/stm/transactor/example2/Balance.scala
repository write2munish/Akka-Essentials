package org.akka.essentials.stm.transactor.example2
import akka.transactor.Transactor
import scala.concurrent.stm.Ref
import scala.concurrent.stm.japi.STM

class Balance(balance: Ref[Float]) extends Transactor {

  def atomically = implicit txn => {
    case message: AccountDebit =>
      if (balance.single.get < message.amount) {
        throw new IllegalStateException("Insufficient Balance");
      } else
        balance transform (_ - message.amount)
    case message: AccountCredit => balance transform (_ + message.amount)
  }

  override def normally: Receive = {
    case message: String =>
      if ("BALANCE".equals(message))
        sender.tell(balance.single.get)
  }
}