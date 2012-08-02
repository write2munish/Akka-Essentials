package org.akka.essentials.zeromq.example4;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.zeromq.Connect;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class PullActor2 extends UntypedActor {

	ActorRef pullSocket = ZeroMQExtension.get(getContext().system())
			.newPullSocket(
					new SocketOption[] { new Connect("tcp://127.0.0.1:1237"),
							new Listener(getSelf()) });
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
			String mesg = new String(m.payload(0));
			log.info("Received Message -> {}",mesg);
		}

	}
}
