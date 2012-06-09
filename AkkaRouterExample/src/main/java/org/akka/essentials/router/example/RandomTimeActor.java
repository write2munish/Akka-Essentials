package org.akka.essentials.router.example;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import akka.actor.UntypedActor;

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
