package org.akka.essentials.stm.transactor.example2
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.transactor.Coordinated
import akka.actor.AllForOneStrategy
import akka.util.duration._
import akka.transactor.CoordinatedTransactionException
import akka.actor.SupervisorStrategy._
import akka.dispatch.Await
import akka.util.Timeout
import akka.pattern.ask
import akka.actor.Props
import akka.transactor.Transactor
import scala.concurrent.stm.Ref

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