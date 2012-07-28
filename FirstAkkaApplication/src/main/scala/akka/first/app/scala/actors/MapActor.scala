package akka.first.app.scala.actors
import java.util.ArrayList
import java.util.StringTokenizer

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import akka.first.app.scala.MapData
import akka.first.app.scala.Word

class MapActor(reduceActor: ActorRef) extends Actor {

	val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be",
		"do", "go", "if", "in", "is", "it", "of", "on", "the", "to")

	def receive: Receive = {
		case message: String =>
			reduceActor ! evaluateExpression(message)
		case _ =>

	}
	def evaluateExpression(line: String): MapData = {
		var dataList = new ArrayList[Word]
		var parser: StringTokenizer = new StringTokenizer(line)
		var defaultCount: Integer = 1
		while (parser.hasMoreTokens()) {
			var word: String = parser.nextToken().toLowerCase()
			if (!STOP_WORDS_LIST.contains(word)) {
				dataList.add(new Word(word, defaultCount))
			}
		}
		return new MapData(dataList)
	}
}
