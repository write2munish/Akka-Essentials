package org.akka.essentials.loadgenerator;


import akka.actor.UntypedActor;

public class JobControllerActor extends UntypedActor {

	int count = 0;
	long startedTime = System.currentTimeMillis();
	int no_of_msgs = 0;

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof String) {
			if (((String) message).compareTo("Done") == 0) {
				count++;
				if (count == no_of_msgs) {
					long now = System.currentTimeMillis();
					System.out.println("All messages processed in "
							+ (now - startedTime) / 1000 + " seconds");

					System.out.println("Total Number of messages processed "
							+ count);
					getContext().system().shutdown();
				}
			}
		}

	}

	public JobControllerActor(int no_of_msgs) {
		this.no_of_msgs = no_of_msgs;
	}
}
