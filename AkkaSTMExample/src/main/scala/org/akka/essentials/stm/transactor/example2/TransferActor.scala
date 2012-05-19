package org.akka.essentials.stm.transactor.example2
import scala.concurrent.stm.Ref

import akka.actor.SupervisorStrategy._
import akka.actor.Actor
import akka.actor.AllForOneStrategy
import akka.actor.Props
import akka.transactor.Coordinated
import akka.transactor.CoordinatedTransactionException
import akka.util.duration._
import akka.util.Timeout
import java.lang.Float

class TransferActor extends Actor {

  val fromAccount = "XYZ";
  val toAccount = "ABC";

  val from = context.actorOf(Props(new Account(fromAccount, Float.parseFloat("5000"))), name = fromAccount)
  val to = context.actorOf(Props(new Account(toAccount, Float.parseFloat("1000"))), name = toAccount)

  override val supervisorStrategy = AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds) {

    case _: CoordinatedTransactionException => Resume
    case _: IllegalStateException => Resume
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }

  def receive: Receive = {
    case message: TransferMsg =>
      implicit val timeout = Timeout(5 seconds)
      val coordinated = Coordinated()
      try {
        coordinated atomic { implicit t ⇒
          to ! coordinated(new AccountCredit(
            message.amtToBeTransferred))
          from ! coordinated(new AccountDebit(
            message.amtToBeTransferred))
        }
      } catch {
        case e: CoordinatedTransactionException =>
        // eat the exception
      }

    case message: AccountBalance =>
      if (message.accountNumber.equalsIgnoreCase(fromAccount)) {
        from.tell(message, sender)
      } else if (message.accountNumber.equalsIgnoreCase(toAccount)) {
        to.tell(message, sender)
      }
    case message: AccountDebit =>
      from.tell(message, sender)
    case message: AccountCredit =>
      from.tell(message, sender)
  }
}