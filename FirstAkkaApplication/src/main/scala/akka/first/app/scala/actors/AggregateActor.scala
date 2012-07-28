package akka.first.app.scala.actors

import java.util.HashMap

import scala.collection.JavaConversions.asScalaSet

import akka.actor.Actor
import akka.first.app.scala.ReduceData
import akka.first.app.scala.Result

class AggregateActor extends Actor {

	var finalReducedMap = new HashMap[String, Integer]

	def receive: Receive = {
		case message: ReduceData =>
			aggregateInMemoryReduce(message.reduceDataMap)
		case message: Result =>
			System.out.println(finalReducedMap.toString())
	}

	def aggregateInMemoryReduce(reducedList: HashMap[String, Integer]) {
		var count: Integer = 0
		for (val key <- reducedList.keySet) {
			if (finalReducedMap.containsKey(key)) {
				count = reducedList.get(key)
				count += finalReducedMap.get(key)
				finalReducedMap.put(key, count)
			} else {
				finalReducedMap.put(key, reducedList.get(key))
			}
		}
	}
}