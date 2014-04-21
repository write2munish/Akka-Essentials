package org.akka.essentials.stm.pingpong.example1;

public class PingPong {
	// state variable identifying whose turn it is.
	private String whoseTurn = null;

	public synchronized boolean hit(String opponent) {

		String x = Thread.currentThread().getName();

		if (whoseTurn == null) {
			whoseTurn = x;
			return true;
		}

		if (x.compareTo(whoseTurn) == 0) {
			System.out.println("PING! (" + x + ")");
			whoseTurn = opponent;
			notifyAll();
		} else {
			try {
				long t1 = System.currentTimeMillis();
				wait(2500);
				if ((System.currentTimeMillis() - t1) > 2500) {
					System.out.println("****** TIMEOUT! " + x
							+ " is waiting for " + whoseTurn + " to play.");
				}
			} catch (InterruptedException e) {
			}
		}

		if (whoseTurn.compareTo("DONE") == 0)
			return false;

		if (opponent.compareTo("DONE") == 0) {
			whoseTurn = opponent;
			notifyAll();
			return false;
		}
		return true; // keep playing.
	}
}
