package org.akka.essentials.custom.extension.example;

import akka.actor.Extension;

import com.typesafe.config.Config;

public class MySQLJDBCSettingsImpl implements Extension {

	public final String DB_URL;
	public final String DB_NAME;
	public final String DB_DRIVER;
	public final String DB_USER_NAME;
	public final String DB_USER_PASSWORD;

	public MySQLJDBCSettingsImpl(Config config) {
		DB_URL = config.getString("connection.db.mysql.url");
		DB_NAME = config.getString("connection.db.mysql.dbname");
		DB_DRIVER = config.getString("connection.db.mysql.driver");
		DB_USER_NAME = config.getString("connection.db.mysql.username");
		DB_USER_PASSWORD = config.getString("connection.db.mysql.userpassword");
	}
}