package akka.first.app.scala.actors
import java.util.ArrayList
import java.util.HashMap

import scala.collection.JavaConversions.asScalaBuffer

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import akka.first.app.scala.MapData
import akka.first.app.scala.ReduceData
import akka.first.app.scala.Word

class ReduceActor(aggregateActor: ActorRef) extends Actor {

  val defaultCount: Int = 1
  def receive: Receive = {
    case message: MapData =>
      aggregateActor ! reduce(message.dataList)
  }

  def reduce(dataList: ArrayList[Word]): ReduceData = {
    var reducedMap = new HashMap[String, Integer]
    for (wc: Word <- dataList) {
      var word: String = wc.word
      if (reducedMap.containsKey(word)) {
        reducedMap.put(word, reducedMap.get(word) + defaultCount)
      } else {
        reducedMap.put(word, defaultCount)
      }
    }
    return new ReduceData(reducedMap)
  }
}