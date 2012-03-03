package tutorial.first.akka;

import akka.actor.UntypedActor;
import static akka.actor.Actors.*;

public class HelloWorldServer extends UntypedActor {

	public void onReceive(Object msg) {
		getContext().tryReply("Hello " + msg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// start the service
		remote().start("localhost", 2552).register("hello-service",
				actorOf(HelloWorldServer.class));

	}

}