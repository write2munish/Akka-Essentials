package org.akka.essentials.stm.pingpong.example2;

import scala.concurrent.stm.Ref;

public class PingPong {

	//updates to Ref.View are synchronous
	Ref.View<String> whoseTurn;

	public PingPong(Ref.View<String> player) {
		whoseTurn = player;
	}

	public boolean hit(final String opponent) {
		final String x = Thread.currentThread().getName();

		if (whoseTurn.get() == "") {
			whoseTurn.set(x);
			return true;
		}

		if (x.compareTo(whoseTurn.get()) == 0) {
			System.out.println("PING! (" + x + ")");
			whoseTurn.set(opponent);
		} else {
			try {
				long t1 = System.currentTimeMillis();
				wait(2500);
				if ((System.currentTimeMillis() - t1) > 2500) {
					System.out.println("****** TIMEOUT! " + x
							+ " is waiting for " + whoseTurn + " to play.");
				}
			} catch (Exception e) {
			}
		}
		if (whoseTurn.get().compareTo("DONE") == 0)
			return false;

		if (opponent.compareTo("DONE") == 0) {
			whoseTurn.set(opponent);
			return false;
		}
		return true; // keep playing.
	}
}
