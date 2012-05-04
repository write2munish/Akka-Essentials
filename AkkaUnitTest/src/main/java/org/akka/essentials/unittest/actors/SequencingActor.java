package org.akka.essentials.unittest.actors;

import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class SequencingActor extends UntypedActor {
	ActorRef next;
	List<Integer> head;
	List<Integer> tail;

	public SequencingActor(ActorRef next, List<Integer> head, List<Integer> tail) {
		this.next = next;
		this.head = head;
		this.tail = tail;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		for (Integer value : head) {
			sender().tell(value);
		}
		sender().tell(message);
		for (Integer value : tail) {
			sender().tell(value);
		}
	}
}
