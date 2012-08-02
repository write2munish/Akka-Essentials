package org.akka.essentials.zeromq.example1;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.zeromq.Connect;
import akka.zeromq.Listener;
import akka.zeromq.Subscribe;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class WorkerTaskA extends UntypedActor {
	ActorRef subSocket = ZeroMQExtension.get(getContext().system())
			.newSubSocket(new Connect("tcp://127.0.0.1:1237"),
					new Listener(getSelf()), new Subscribe("someTopic"));

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
			String mesg = new String(m.payload(1));
			log.info("Received Message @ A -> {}",mesg);
		}
	}
}
