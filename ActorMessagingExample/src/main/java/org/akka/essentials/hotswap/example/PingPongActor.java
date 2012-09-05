package org.akka.essentials.hotswap.example;

import akka.actor.UntypedActor;
import akka.japi.Procedure;

public class PingPongActor extends UntypedActor {

	static String PING = "PING";
	static String PONG = "PONG";
	int count = 0;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			if (((String) message).matches(PING)) {
				System.out.println("PING");
				count += 1;
				Thread.sleep(100);
				getSelf().tell(PONG);
				getContext().become(new Procedure<Object>() {
					public void apply(Object message) {
						if (message instanceof String) {
							if (((String) message).matches(PONG)) {
								System.out.println("PONG");
								count += 1;
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									//
								}
								getSelf().tell(PING);
								getContext().unbecome();
							}
						}
					}
				});
				if (count > 10)
					getContext().stop(getSelf());
			}
		}
	}
}
