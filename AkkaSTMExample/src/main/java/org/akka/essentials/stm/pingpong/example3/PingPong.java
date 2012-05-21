package org.akka.essentials.stm.pingpong.example3;

import akka.agent.Agent;
import akka.util.Timeout;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PingPong {

	Agent<String> whoseTurn;

	public PingPong(Agent<String> player) {
		whoseTurn = player;
	}

	public boolean hit(final String opponent) {
		final String x = Thread.currentThread().getName();

		//wait till all the messages are processed to make 
		//you get the correct value, as updated to Agents are
		//async
		String result = whoseTurn.await(new Timeout(5, SECONDS));
		
		if (result == "") {
			whoseTurn.send(x);
			return true;
		}

		if (x.compareTo(result) == 0) {
			System.out.println("PING! (" + x + ")");
			whoseTurn.send(opponent);
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
		if (result.compareTo("DONE") == 0)
			return false;

		if (opponent.compareTo("DONE") == 0) {
			whoseTurn.send(opponent);
			return false;
		}
		return true; // keep playing.
	}
}
