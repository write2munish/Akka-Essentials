package tutorial.first.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import static akka.actor.Actors.*;

/**
 * Hello Munish!
 *
 */
public class HelloWorld extends UntypedActor
{
    public static void main( String[] args )
    {
        //create and start the actor
    	ActorRef actor = actorOf(HelloWorld.class).start();
        
        //send the message to the actor and wait for response
    	Object response = actor.ask("Munish").get();
        
    	//print the response
        System.out.println(response);
        
        //stop the actor
        actor.stop();
    }

	@Override
	public void onReceive(Object message) throws Exception {
		
		//receive and reply to the message received
		getContext().tryReply("Hello " + message);
		
	}
}
