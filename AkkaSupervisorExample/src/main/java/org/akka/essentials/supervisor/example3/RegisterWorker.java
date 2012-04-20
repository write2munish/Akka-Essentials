package org.akka.essentials.supervisor.example3;

import akka.actor.ActorRef;

public class RegisterWorker {
	ActorRef worker;
	ActorRef supervisor;

	public RegisterWorker(ActorRef worker, ActorRef supervisor) {
		this.worker = worker;
		this.supervisor = supervisor;
	}

	public ActorRef getWorker() {
		return worker;
	}

	public ActorRef getSupervisor() {
		return supervisor;
	}
}
