package org.akka.essentials.stm.transactor.example2

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.OneForOneStrategy
import akka.actor.Props
import akka.actor.SupervisorStrategy._
import akka.pattern.ask
import akka.transactor.CoordinatedTransactionException
import akka.util.Timeout

class BankActor extends Actor with ActorLogging {

  val transferActor = context.actorOf(Props[TransferActor], name = "TransferActor")
  implicit val timeout = Timeout(5 seconds)

  def receive = {
    case transfer: TransferMsg =>
      transferActor ! transfer
    case balance: AccountBalance =>
      val future = (transferActor ? balance).mapTo[AccountBalance]
      val account = Await.result(future, timeout.duration)
      println("Account #" + account.accountNumber + " , Balance #" + account.accountBalance)
  }

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds) {
    case _: CoordinatedTransactionException => Resume
    case _: IllegalStateException => Resume
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }
}