package org.akka.essentials.serializer;

import akka.actor.ActorSystem;
import akka.serialization.Serialization;
import akka.serialization.SerializationExtension;
import akka.serialization.Serializer;

import com.typesafe.config.ConfigFactory;

/**
 * Hello world!
 * 
 */
public class MySerializationApp {

	public static void main(String[] args) {

		ActorSystem system = ActorSystem.create("MySerializableSys",
				ConfigFactory.load().getConfig("MySerializableSys"));
		
		Serialization serialization = SerializationExtension.get(system);

		MyMessage originalMessage = new MyMessage("Munish", 36, "Bangalore");

		System.out.println("The original message is as " + originalMessage);

		// Get the Binded Serializer for it
		Serializer serializer = serialization
				.findSerializerFor(originalMessage);

		// Turn the object into bytes
		byte[] bytes = serializer.toBinary(originalMessage);

		// Turn the byte[] back into an object,
		MyMessage deSerializedMessage = (MyMessage) serializer.fromBinary(
				bytes, MyMessage.class);

		System.out.println("The de-serialized message is as " + deSerializedMessage);

		system.shutdown();
	}

}
