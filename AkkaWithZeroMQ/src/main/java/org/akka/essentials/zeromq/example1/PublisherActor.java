package org.akka.essentials.zeromq.example1;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.util.Duration;
import akka.zeromq.Bind;
import akka.zeromq.Frame;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class PublisherActor extends UntypedActor {
	public static final Object TICK = "TICK";
	int count = 0;
	Cancellable cancellable;
	ActorRef pubSocket = ZeroMQExtension.get(getContext().system())
			.newPubSocket(new Bind("tcp://127.0.0.1:1237"));

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
			pubSocket.tell(new ZMQMessage(new Frame("someTopic"), new Frame(
					"This is the workload " + ++count)));
			
			if(count==10) 
				cancellable.cancel();
		}
	}
}
