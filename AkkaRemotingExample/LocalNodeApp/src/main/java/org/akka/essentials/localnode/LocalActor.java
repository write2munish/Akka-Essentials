package org.akka.essentials.localnode;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;

public class LocalActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	Timeout timeout = new Timeout(Duration.parse("5 seconds"));

	ActorRef remoteActor;

	@Override
	public void preStart() {
		//Get a reference to the remote actor
		remoteActor = getContext().actorFor(
				"akka://RemoteNodeApp@10.101.161.20:2552/user/remoteActor");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		Future<Object> future = Patterns.ask(remoteActor, message.toString(),
				timeout);
		String result = (String) Await.result(future, timeout.duration());
		log.info("Message received from Server -> {}", result);
	}
}
