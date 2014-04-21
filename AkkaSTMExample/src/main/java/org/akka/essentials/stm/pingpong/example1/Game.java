package org.akka.essentials.stm.pingpong.example1;

public class Game {
	public static void main(String args[]) {
		PingPong table = new PingPong();
		Thread alice = new Thread(new Player("bob", table));
		Thread bob = new Thread(new Player("alice", table));

		alice.setName("alice");
		bob.setName("bob");
		alice.start(); // alice starts playing
		bob.start(); // bob starts playing
		try {
			// Wait 5 seconds
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		table.hit("DONE"); // cause the players to quit their threads.
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	}
}
