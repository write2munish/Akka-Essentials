package org.akka.essentials.stm.transactor.example
import scala.concurrent.stm.Ref
import akka.actor.Actor
import akka.transactor.Coordinated
import akka.actor.ActorLogging

class AccountActor(accountNumber: String, inBalance: Float) extends Actor {

  val balance = Ref(inBalance)

  def receive = {
    case value: AccountBalance =>
      sender ! new AccountBalance(accountNumber, balance.single.get)

    case coordinated @ Coordinated(message: AccountDebit) =>
      // coordinated atomic ...
      coordinated atomic { implicit t =>
        //check for funds availability
        if (balance.get(t) > message.amount)
          balance.transform(_ - message.amount)
        else
          throw new IllegalStateException(
            "Insufficient Balance")
      }
    case coordinated @ Coordinated(message: AccountCredit) =>
      // coordinated atomic ...
      coordinated atomic { implicit t =>
        balance.transform(_ + message.amount)
      }
  }
}