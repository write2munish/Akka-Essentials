package org.akka.essentials.future.example;

import org.akka.essentials.future.example.messages.Address;

import akka.actor.UntypedActor;

public class AddressActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Integer) {
			Integer userId = (Integer) message;
			// ideally we will get address for given user id
			Address address = new Address(userId, "Munish Gupta",
					"Sarjapura Road", "Bangalore, India");
			getSender().tell(address);
		}
	}
}