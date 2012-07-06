package org.akka.essentials.hotswap
import akka.actor.Actor
import akka.actor.ActorLogging

case class ADD
case class SUBTRACT
case class MULTIPLY
case class DIVIDE
case class Numbers(first: Integer, second: Integer)

class CalculatorActor extends Actor with ActorLogging {

	override def preStart() {
		context.become(add)
	}

	def receive = {
		case _ => log.info("unknown message")
	}

	def add: Receive = {
		case number: Numbers => sender ! number.first + number.second
		case "Minus" => 
		case msg: Object => checkAndUpdate(msg.toString())
	}

	def subtract: Receive = {
		case number: Numbers => sender ! number.first - number.second
		case msg: Object => checkAndUpdate(msg.toString())
	}

	def multiply: Receive = {
		case number: Numbers => sender ! number.first * number.second
		case msg: Object => checkAndUpdate(msg.toString())
	}

	def divide: Receive = {
		case number: Numbers => sender ! number.first / number.second
		case msg: Object => checkAndUpdate(msg.toString())
	}

	def checkAndUpdate: Receive = {
		case "ADD" =>
			context.unbecome()
			context.become(add)
		case "SUBTRACT" =>
			context.unbecome()
			context.become(subtract)
		case "MULTIPLY" =>
			context.unbecome()
			context.become(multiply)
		case "DIVIDE" =>
			context.unbecome()
			context.become(divide)
	}
}