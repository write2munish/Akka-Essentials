package org.akka.essentials.grid;

import java.io.Serializable;

public class StartWorker implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9064403102760025633L;
	String actorPath;

	public StartWorker(String inActorPath) {
		actorPath = inActorPath;
	}

	public String getActorPath() {
		return actorPath;
	}
}
