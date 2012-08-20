package org.akka.essentials.custom.extension.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.ConfigFactory;

public class TestApp {

	public TestApp() {

		ActorSystem _system = ActorSystem.create("Extension-Test",
				ConfigFactory.load().getConfig("TestApp"));

		ActorRef ref = _system.actorOf(new Props(MyActor.class));
		
		ref.tell("msg");

		MySQLJDBCSettingsImpl mysqlSetting = MySQLJDBCSettings.SettingsProvider
				.get(_system);

		System.out.println(mysqlSetting.DB_NAME);
		System.out.println(mysqlSetting.DB_URL);

		_system.shutdown();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestApp();

	}

}
