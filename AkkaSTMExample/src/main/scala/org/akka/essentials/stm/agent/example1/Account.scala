package org.akka.essentials.stm.agent.example1
import scala.concurrent.stm.Ref
import akka.actor.Actor
import akka.transactor.Coordinated
import akka.actor.ActorLogging
import akka.agent.Agent
import java.lang.Float
import akka.util.Timeout
import akka.util.duration._

class Account(accountNumber: String, balance: Agent[Float]) extends Actor {

	def receive = {
		case value: AccountBalance =>
			//adding the time out to make sure all the updates have been 
			//applied before reading
			implicit val timeout = Timeout(5 seconds)
			sender ! new AccountBalance(accountNumber, balance.await)
		case coordinated @ Coordinated(message: AccountDebit) => {
			// coordinated atomic ...
			coordinated atomic { implicit t ⇒
				{
					//check for funds availability
					if (balance.get() > message.amount) {
						balance.send(balance.get() - message.amount)
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
				balance.send(balance.get() + message.amount)
			}
		}
	}
}