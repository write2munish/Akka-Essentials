package tutorial.first.akka;

import akka.actor.ActorRef;
import static akka.actor.Actors.*;

public class HelloWorldClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// client code
		ActorRef actor = remote().actorFor(
		  "hello-service", "localhost", 2552);
		Object response = actor.ask("Munish").get(); 
		System.out.println(response);
	}

}