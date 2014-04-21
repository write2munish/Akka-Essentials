package org.akka.essentials.stm.transactor.example2

import scala.concurrent.stm.Ref

import akka.actor.actorRef2Scala
import akka.transactor.Transactor

class AccountActor(accountNumber: String, inBalance: Float) extends Transactor {

  val balance = Ref(inBalance)

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
    case value: AccountBalance =>
      sender ! new AccountBalance(accountNumber, balance.single.get)
  }

}