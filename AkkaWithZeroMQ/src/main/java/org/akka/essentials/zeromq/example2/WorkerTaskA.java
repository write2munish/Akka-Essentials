package org.akka.essentials.zeromq.example2;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.zeromq.Connect;
import akka.zeromq.Frame;
import akka.zeromq.Identity;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class WorkerTaskA extends UntypedActor {
	ActorRef subSocket = ZeroMQExtension.get(getContext().system())
			.newDealerSocket(
					new SocketOption[] { new Connect("tcp://127.0.0.1:1237"),
							new Listener(getSelf()),
							new Identity("A".getBytes()) });
	
	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
			String mesg = new String(m.payload(0));
			subSocket.tell((new ZMQMessage(new Frame(mesg
					+ " Processed the workload for A"))));
		}

	}

}
