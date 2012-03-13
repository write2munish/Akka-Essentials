package org.akka.essentials.wc.mapreduce.example.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import akka.actor.UntypedActor;

public class AggregateActor extends UntypedActor {

	private Map<String, Integer> finalReducedMap = new HashMap<String, Integer>();

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Integer> reducedList = (Map<String, Integer>) message;
			aggregateInMemoryReduce(reducedList);
		} else if (message instanceof String) {
			if (((String) message).compareTo("DISPLAY_LIST") == 0) {
				//getSender().tell(finalReducedMap.toString());
				System.out.println(finalReducedMap.toString());
				
			}
		}
	}

	private void aggregateInMemoryReduce(Map<String, Integer> reducedList) {

		Iterator<String> iter = reducedList.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (finalReducedMap.containsKey(key)) {
				Integer count = reducedList.get(key) + finalReducedMap.get(key);
				finalReducedMap.put(key, count);
			} else {
				finalReducedMap.put(key, reducedList.get(key));
			}

		}
	}

}
