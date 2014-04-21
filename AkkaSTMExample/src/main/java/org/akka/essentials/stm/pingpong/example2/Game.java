package org.akka.essentials.stm.pingpong.example2;

import akka.actor.ActorSystem;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

public class Game {
	public static void main(String args[]) {

		ActorSystem _system = ActorSystem.create("Agent-example");
		Ref.View<String> turn = STM.newRef("");
		PingPong table = new PingPong(turn);

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
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		_system.shutdown();
	}
}
