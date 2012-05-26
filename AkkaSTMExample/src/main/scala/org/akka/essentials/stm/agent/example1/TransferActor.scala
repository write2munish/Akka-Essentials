package org.akka.essentials.stm.agent.example1
import scala.concurrent.stm.Ref
import akka.actor.SupervisorStrategy._
import akka.actor.Actor
import akka.actor.AllForOneStrategy
import akka.actor.Props
import akka.transactor.Coordinated
import akka.transactor.CoordinatedTransactionException
import akka.util.duration._
import akka.util.Timeout
import akka.agent.Agent
import java.lang.Float

class TransferActor extends Actor {

  val fromAccount = "XYZ";
  val toAccount = "ABC";

  val from = context.actorOf(Props(new Account(fromAccount, Agent(Float.valueOf("5000"))(context.system))), name = fromAccount)
  val to = context.actorOf(Props(new Account(toAccount, Agent(Float.valueOf("1000"))(context.system))), name = toAccount)

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

          from ! coordinated(new AccountDebit(
            message.amtToBeTransferred))
            
          to ! coordinated(new AccountCredit(
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
  }

}