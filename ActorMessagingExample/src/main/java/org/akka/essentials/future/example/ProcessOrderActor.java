package org.akka.essentials.future.example;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.akka.essentials.future.example.messages.Address;
import org.akka.essentials.future.example.messages.Order;
import org.akka.essentials.future.example.messages.OrderHistory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Future;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.util.Duration;
import akka.util.Timeout;

public class ProcessOrderActor extends UntypedActor {

	final Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));
	ActorRef orderActor = getContext().actorOf(new Props(OrderActor.class));
	ActorRef addressActor = getContext().actorOf(new Props(AddressActor.class));
	ActorRef orderAggregateActor = getContext().actorOf(new Props(OrderAggregateActor.class));
	
	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Integer) {
			Integer userId = (Integer) message;
			final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();
			//make concurrent calls to actors
			futures.add(ask(orderActor, userId, t));
			futures.add(ask(addressActor, userId, t));

			//set the sequence in which the reply are expected 
			final Future<Iterable<Object>> aggregate = Futures.sequence(
					futures, getContext().system().dispatcher());

			//once the replies comes back, we loop through the Iterable to
			//get the replies in same order
			final Future<OrderHistory> aggResult = aggregate
					.map(new Mapper<Iterable<Object>, OrderHistory>() {
						public OrderHistory apply(Iterable<Object> coll) {
							final Iterator<Object> it = coll.iterator();
							final Order order = (Order) it.next();
							final Address address = (Address) it.next();
							return new OrderHistory(order, address);
						}
					});
			//aggregated result is piped to another actor 
			pipe(aggResult).to(orderAggregateActor);
		}
	}
}
