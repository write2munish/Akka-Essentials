package org.akka.essentials.grid;

import java.io.Serializable;

public class Task implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4254133675360422392L;
	private int taskNumber;

	public Task(int inTaskNumber) {
		taskNumber = inTaskNumber;
	}

	public int getTaskNumber() {
		return taskNumber;
	}
}
