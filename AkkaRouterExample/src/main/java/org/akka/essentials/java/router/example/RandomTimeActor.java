package org.akka.essentials.java.router.example;

import akka.actor.UntypedActor;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RandomTimeActor extends UntypedActor {

	Random randomGenerator = new Random();

	@Override
	public void onReceive(Object msg) throws Exception {
		int sleepTime = randomGenerator.nextInt(5);
		System.out.println(String.format("Actor # '%s' sleeping for '%s'",
				getSelf().path().name(), sleepTime));
		TimeUnit.SECONDS.sleep(sleepTime);
		getSender().tell("Message from Actor #" + getSelf().path());
	}

}
