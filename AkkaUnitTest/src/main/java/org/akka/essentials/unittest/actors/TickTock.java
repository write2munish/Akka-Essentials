package org.akka.essentials.unittest.actors;

import akka.actor.UntypedActor;

public class TickTock extends UntypedActor {

	public static class Tick {};
	public static class Tock {};
	public boolean state = false;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Tick) {
			tick_this(message);
		} else if (message instanceof Tock) {
			tock_this(message);
		} else
			unhandled(message);
	}

	public void tock_this(Object message) {
		// do some processing here
		if (state == false)
			state = true;
		else
			state = false;
	}

	public void tick_this(Object message) {
		// do some processing here
		sender().tell("processed the tick message");
	}
}
