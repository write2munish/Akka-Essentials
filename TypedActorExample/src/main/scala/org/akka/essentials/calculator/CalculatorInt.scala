package org.akka.essentials.calculator
import akka.dispatch.Future
import akka.actor.TypedActor.Receiver
import akka.actor.TypedActor.PreStart
import akka.actor.TypedActor.Supervisor
import akka.actor.TypedActor.PostStop


trait CalculatorInt extends Receiver  {

	def add(first: Int, second: Int): Future[Int]

	def subtract(first: Int, second: Int): Future[Int]

	def incrementCount(): Unit

	def incrementAndReturn(): Option[Int]
}
