package org.akka.essentials.zeromq.example3.server;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.zeromq.Bind;
import akka.zeromq.Frame;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class ServerActor extends UntypedActor {
	ActorRef repSocket = ZeroMQExtension.get(getContext().system())
			.newRepSocket(
					new SocketOption[] { new Bind("tcp://127.0.0.1:1237"),
							new Listener(getSelf()) });

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
			String mesg = new String(m.payload(0));

			repSocket.tell((new ZMQMessage(
					new Frame(mesg + " Good to see you!"))));
		}
	}
}
