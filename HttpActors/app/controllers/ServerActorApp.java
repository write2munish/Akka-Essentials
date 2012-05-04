package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import static akka.pattern.Patterns.ask;
import play.libs.Akka;
import akka.actor.*;
import play.libs.F.Function;
import org.akka.essentials.remoteActor.sample.ServerActor;

public class ServerActorApp extends Controller {
  
  private static ActorRef myServerActor = Akka.system().actorOf(new Props(ServerActor.class));
   
  public static Result process(String msg){
  
  	return async(
    Akka.asPromise(ask(myServerActor,msg, 1000)).map(
      new Function<Object,Result>() {
        public Result apply(Object response) {
          return ok(response.toString());
        }
      }
    )
  );
  }
}