package org.akka.essentials.zeromq.example3.client;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Duration;
import akka.zeromq.Connect;
import akka.zeromq.Frame;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class ClientActor extends UntypedActor {
	public static final Object TICK = "TICK";
	int count = 0;
	Cancellable cancellable;
	ActorRef reqSocket = ZeroMQExtension.get(getContext().system())
			.newReqSocket(
					new SocketOption[] { new Connect("tcp://127.0.0.1:1237"),
							new Listener(getSelf()) });
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void preStart() {
		cancellable = getContext()
				.system()
				.scheduler()
				.schedule(Duration.parse("1 second"),
						Duration.parse("1 second"), getSelf(), TICK);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals(TICK)) {
			// send a message to the replier system
			reqSocket.tell(new ZMQMessage(new Frame("Hi there! ("
					+ getContext().self().hashCode() + ")->")));
			count++;
			if (count == 10)
				cancellable.cancel();
		} else if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
			String mesg = new String(m.payload(0));

			log.info("Received msg! {}", mesg);
		}
	}
}
