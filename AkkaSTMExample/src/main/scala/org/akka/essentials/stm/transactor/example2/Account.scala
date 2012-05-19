package org.akka.essentials.stm.transactor.example2
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.transactor.Coordinated
import akka.actor.OneForOneStrategy
import akka.util.duration._
import akka.transactor.CoordinatedTransactionException
import akka.actor.SupervisorStrategy._
import akka.dispatch.Await
import akka.util.Timeout
import akka.pattern.ask
import akka.actor.Props
import scala.concurrent.stm.Ref

class Account(accountNumber: String, accBalance: Float) extends Actor with ActorLogging {

  val balance = context.actorOf(Props(new Balance(Ref(accBalance))), name = "balance")

  def receive = {
    case value: AccountBalance =>
      implicit val timeout = Timeout(5 seconds)
      val future = balance ? "BALANCE"
      val balanceVal = Await.result(future, timeout.duration).asInstanceOf[Float]
      sender ! new AccountBalance(accountNumber, balanceVal)
    case message: Coordinated =>
      balance.tell(message)
    case message: AccountDebit =>
      balance.tell(message, sender)
    case message: AccountCredit =>
      balance.tell(message, sender)
  }

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds) {
    case _: CoordinatedTransactionException => Resume
    case _: IllegalStateException => Resume
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }
}