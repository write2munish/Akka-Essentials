package org.akka.essentials.custom.extension.example;

import akka.actor.UntypedActor;

public class MyActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		MySQLJDBCSettingsImpl mysqlSetting = MySQLJDBCSettings.SettingsProvider
				.get(getContext().system());

		System.out.println(mysqlSetting.DB_USER_NAME);
		System.out.println(mysqlSetting.DB_USER_PASSWORD);

	}

}
