package org.akka.essentials.serializer;

import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.kernel.Bootable;
import akka.serialization.Serialization;
import akka.serialization.SerializationExtension;
import akka.serialization.Serializer;

import com.typesafe.config.ConfigFactory;

/**
 * Hello world!
 * 
 */
public class MySerializationApp implements Bootable {
	private ActorSystem system = ActorSystem.create("MySerializableSys", ConfigFactory.load()
			.getConfig("MySerializableSys"));
	private LoggingAdapter log = Logging.getLogger(system, this);;

	public MySerializationApp() {

		//log = Logging.getLogger(system, this);

		Serialization serialization = SerializationExtension.get(system);

		MyMessage originalMessage = new MyMessage("Munish", 36, "Bangalore");

		log.info("The original message is as " + originalMessage);

		// Get the Binded Serializer for it
		Serializer serializer = serialization
				.findSerializerFor(originalMessage);

		// Turn the object into bytes
		byte[] bytes = serializer.toBinary(originalMessage);

		// Turn the byte[] back into an object,
		MyMessage deSerializedMessage = (MyMessage) serializer.fromBinary(
				bytes, MyMessage.class);

		log.info("The de-serialized message is as " + deSerializedMessage);
	}

	public static void main(String[] args) {
		MySerializationApp app = new MySerializationApp();

		app.shutdown();

	}

	public void shutdown() {
		system.shutdown();

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

}
