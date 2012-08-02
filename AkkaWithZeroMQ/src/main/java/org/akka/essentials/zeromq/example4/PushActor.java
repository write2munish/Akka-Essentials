package org.akka.essentials.zeromq.example4;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.util.Duration;
import akka.zeromq.Bind;
import akka.zeromq.Frame;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class PushActor extends UntypedActor {
	public static final Object TICK = "TICK";
	int count = 0;
	Cancellable cancellable;
	ActorRef pushSocket = ZeroMQExtension.get(getContext().system())
			.newPushSocket(
					new SocketOption[] { new Bind("tcp://127.0.0.1:1237"),
							new Listener(getSelf()) });

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

			count++;
			pushSocket.tell(new ZMQMessage(
					new Frame("Hi there (" + count + ")")));
			if (count == 5)
				cancellable.cancel();
		}

	}
}
