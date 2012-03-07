package org.akka.essentials.clientserver.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class ClientActorSystem implements Bootable {

	private ActorSystem system;
    private ActorRef actor;
    private ActorRef remoteActor;
    
	public ClientActorSystem(){
        system = ActorSystem.create("LookupApplication", ConfigFactory.load().getConfig("ClientActor"));

        remoteActor = system.actorFor("akka://HelloWorldApplication@127.0.0.1:2552/user/serverActor");
        
        actor = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new ClientActor(remoteActor);
			}
		}));
        
        actor.tell("Start");
	}

	public void shutdown() {
		// TODO Auto-generated method stub
		system.shutdown();
	}


	public void startup() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		
		new ClientActorSystem();
	}

}
