package org.akka.essentials.grid;

import java.io.Serializable;

public class TaskFinished implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 970605290206333478L;
	int taskNumber;

	public TaskFinished(int inTaskNumber) {
		taskNumber = inTaskNumber;
	}

	public int getTaskNumber() {
		return taskNumber;
	}
}
