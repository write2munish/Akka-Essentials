package org.akka.essentials.serializerScala


import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.serialization.SerializationExtension

object MySerializationApp {

  def main(args: Array[String]): Unit = {}
  val system = ActorSystem.create("MySerializableSys", ConfigFactory.load()
    .getConfig("MySerializableSys"));
  val log = system.log

  // Get the Serialization Extension
  val serialization = SerializationExtension(system)

  val originalMessage = new MyMessage("Munish", 36, "Bangalore")

  log.info("The original message is as " + originalMessage)

  // Get the Binded Serializer for it
  val serializer = serialization
    .findSerializerFor(originalMessage);

  // Turn the object into bytes
  val bytes = serializer.toBinary(originalMessage);

  // Turn the byte[] back into an object,
  val deSerializedMessage = serializer.fromBinary(
    bytes, classOf[MyMessage])

  log.info("The de-serialized message is as " + deSerializedMessage);

}