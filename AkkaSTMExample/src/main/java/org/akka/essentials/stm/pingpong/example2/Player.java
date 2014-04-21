package org.akka.essentials.stm.pingpong.example2;

public class Player implements Runnable {
	PingPong myTable; // Table where they play
	String myOpponent;

	public Player(String opponent, PingPong table) {
		myTable = table;
		myOpponent = opponent;
	}

	public void run() {
		while (myTable.hit(myOpponent))
			;
	}
}
