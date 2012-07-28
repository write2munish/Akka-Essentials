package akka.first.app.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.first.app.java.actors.MasterActor;
import akka.first.app.java.messages.Result;

public class MapReduceApplication {

	public static void main(String[] args) throws Exception {

		ActorSystem _system = ActorSystem.create("MapReduceApp");
		
		ActorRef master = _system.actorOf(new Props(MasterActor.class),"master");
		
		master.tell("The quick brown fox tried to jump over the lazy dog and fell on the dog");
		master.tell("Dog is man's best friend");
		master.tell("Dog and Fox belong to the same family");
		
		Thread.sleep(500);
		
		master.tell(new Result());
		
		Thread.sleep(500);
		
		_system.shutdown();
	}
}
