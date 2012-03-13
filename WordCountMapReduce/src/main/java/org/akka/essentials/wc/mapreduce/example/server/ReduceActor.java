package org.akka.essentials.wc.mapreduce.example.server;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class ReduceActor extends UntypedActor {
	private ActorRef actor = null;

	public ReduceActor(ActorRef inAggregateActor) {
		actor = inAggregateActor;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof List) {

			@SuppressWarnings("unchecked")
			List<Result> work = (List<Result>) message;

			// perform the work
			NavigableMap<String, Integer> reducedList = reduce(work);

			// reply with the result
			actor.tell(reducedList);

		} else
			throw new IllegalArgumentException("Unknown message [" + message
					+ "]");
	}

	private NavigableMap<String, Integer> reduce(List<Result> list) {

		NavigableMap<String, Integer> reducedMap = new ConcurrentSkipListMap<String, Integer>();

		Iterator<Result> iter = list.iterator();
		while (iter.hasNext()) {
			Result result = iter.next();
			if (reducedMap.containsKey(result.getWord())) {
				Integer value = (Integer) reducedMap.get(result.getWord());
				value++;
				reducedMap.put(result.getWord(), value);
			} else {
				reducedMap.put(result.getWord(), Integer.valueOf(1));
			}
		}
		return reducedMap;
	}
}
