package org.akka.essentials.stm.pingpong.example3;

import akka.actor.ActorSystem;
import akka.agent.Agent;

public class Game {
	public static void main(String args[]) {

		ActorSystem _system = ActorSystem.create("Agent-example");
		Agent<String> turn = new Agent<String>("", _system);
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
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		_system.shutdown();
	}
}
