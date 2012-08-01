package org.akka.essentials.serializer
import akka.serialization.SerializationExtension
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

case class MyMessage( name: String, age: Int, city: String) 

object MySerializationApp {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("MySerializableSys", ConfigFactory.load()
      .getConfig("MySerializableSys"));
    val log = system.log

    // Get the Serialization Extension
    val serialization = SerializationExtension(system)

    val originalMessage = new MyMessage("Munish", 36, "Bangalore")

    log.info("The original message is as {}", originalMessage)

    // Get the Binded Serializer for it
    val serializer = serialization
      .findSerializerFor(originalMessage);

    // Turn the object into bytes
    val bytes = serializer.toBinary(originalMessage);

    // Turn the byte[] back into an object,
    val deSerializedMessage = serializer.fromBinary(
      bytes, classOf[MyMessage])

    log.info("The de-serialized message is as {}",deSerializedMessage);
    
    system.shutdown
  }
}