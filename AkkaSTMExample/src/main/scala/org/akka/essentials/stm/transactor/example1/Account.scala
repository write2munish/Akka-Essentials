package org.akka.essentials.stm.transactor.example1
import scala.concurrent.stm.Ref
import akka.actor.Actor
import akka.transactor.Coordinated
import akka.actor.ActorLogging

class Account(accountNumber: String, balance: Ref[Float]) extends Actor with ActorLogging {

  override def preStart() {
    log.info("Starting Account account# {} with balance# {}", accountNumber, balance.single.get)
  }

  def receive = {
    case value: AccountBalance =>
      sender ! new AccountBalance(accountNumber, balance.single.get)
    case coordinated @ Coordinated(message: AccountDebit) => {
      // coordinated atomic ...
      coordinated atomic { implicit t ⇒
        {
          //check for funds availability
          if (balance.get(t) > message.amount) {
            val bal: Float = balance.get(t) - message.amount
            balance.set(bal)
          } else {
            throw new IllegalStateException(
              "Insufficient Balance")
          }
        }
      }
    }
    case coordinated @ Coordinated(message: AccountCredit) => {
      // coordinated atomic ...
      coordinated atomic { implicit t ⇒
        {
          val bal: Float = balance.get(t) + message.amount
          balance.set(bal)
        }
      }
    }
  }
}