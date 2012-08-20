package org.akka.essentials.unittest.actors;

import akka.actor.UntypedActor;

public class TickTock extends UntypedActor {

	public static class Tick {
		String message;
		public Tick(String inStr) {
			message = inStr;
		}
	};

	public static class Tock {
		String message;
		public Tock(String inStr) {
			message = inStr;
		}
	};

	public boolean state = false;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Tick) {
			tick((Tick) message);
		} else if (message instanceof Tock) {
			tock((Tock) message);
		} else
			throw new IllegalArgumentException("boom!");
	}

	public void tock(Tock message) {
		// do some processing here
		if (state == false)
			state = true;
		else
			state = false;
	}

	public void tick(Tick message) {
		// do some processing here
		sender().tell("processed the tick message");
	}
}
