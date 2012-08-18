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

class AccountActor(accountNumber: String, accBalance: Float) extends Actor {

  val balance = context.actorOf(Props[BalanceTransactor], name = "BalanceActor")
  implicit val timeout = Timeout(1 seconds)
  override def preStart() {
    balance ! new AccountCredit(accBalance)
  }

  def receive = {
    case value: AccountBalance =>
      val future = (balance ? "BALANCE").mapTo[Float]
      val balanceVal = Await.result(future, timeout.duration)
      sender ! new AccountBalance(accountNumber, balanceVal)
    case coordinated: Coordinated =>
      val message = coordinated.getMessage()
      coordinated atomic { implicit t =>
        balance ! coordinated(message)
      }
    case message: AccountDebit =>
      balance.tell(message, sender)
    case message: AccountCredit =>
      balance.tell(message, sender)
  }

  override val supervisorStrategy = AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds) {
    case _: CoordinatedTransactionException => Resume
    case _: IllegalStateException => Resume
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }
}