package org.akka.essentials.zeromq.example2;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.serialization.Serialization;
import akka.serialization.SerializationExtension;
import akka.zeromq.Connect;
import akka.zeromq.Frame;
import akka.zeromq.Identity;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class WorkerTaskB extends UntypedActor {
	ActorRef subSocket = ZeroMQExtension.get(getContext().system())
			.newDealerSocket(
					new SocketOption[] { new Connect("tcp://127.0.0.1:1237"),
							new Listener(getSelf()),
							new Identity("B".getBytes()) });
	Serialization ser = SerializationExtension.get(getContext().system());
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	int count = 0;

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
			String mesg = new String(m.payload(0));
			count++;
		//	log.info("Received Message @ B ->" + mesg);
			subSocket.tell((new ZMQMessage(new Frame(mesg
					+ " Processed the workload for B"))));
		}

	}

}
