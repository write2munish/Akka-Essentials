package org.akka.essentials.grid;

import java.io.Serializable;

public class StopWorker implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7202366558361921698L;
	String actorPath;

	public StopWorker(String inActorPath) {
		actorPath = inActorPath;
	}

	public String getActorPath() {
		return actorPath;
	}

}
