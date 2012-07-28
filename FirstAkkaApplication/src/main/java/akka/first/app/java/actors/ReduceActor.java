package akka.first.app.java.actors;

import java.util.HashMap;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.first.app.java.messages.MapData;
import akka.first.app.java.messages.ReduceData;
import akka.first.app.java.messages.WordCount;

public class ReduceActor extends UntypedActor {

	private ActorRef aggregateActor = null;

	public ReduceActor(ActorRef inAggregateActor) {
		aggregateActor = inAggregateActor;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof MapData) {
			MapData mapData = (MapData) message;
			// reduce the incoming data
			ReduceData reduceData = reduce(mapData.getDataList());
			// forward the result to aggregate actor
			aggregateActor.tell(reduceData);
		} else
			unhandled(message);
	}

	private ReduceData reduce(List<WordCount> dataList) {
		HashMap<String, Integer> reducedMap = new HashMap<String, Integer>();
		for (WordCount wordCount : dataList) {
			if (reducedMap.containsKey(wordCount.getWord())) {
				Integer value = (Integer) reducedMap.get(wordCount.getWord());
				value++;
				reducedMap.put(wordCount.getWord(), value);
			} else {
				reducedMap.put(wordCount.getWord(), Integer.valueOf(1));
			}
		}
		return new ReduceData(reducedMap);
	}
}
