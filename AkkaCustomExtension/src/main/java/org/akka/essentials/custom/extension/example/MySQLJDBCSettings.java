package org.akka.essentials.custom.extension.example;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtendedActorSystem;
import akka.actor.ExtensionIdProvider;

public class MySQLJDBCSettings extends
		AbstractExtensionId<MySQLJDBCSettingsImpl> implements
		ExtensionIdProvider {

	public final static MySQLJDBCSettings SettingsProvider = new MySQLJDBCSettings();

	public MySQLJDBCSettings lookup() {
		return MySQLJDBCSettings.SettingsProvider;
	}

	public MySQLJDBCSettingsImpl createExtension(ExtendedActorSystem system) {
		return new MySQLJDBCSettingsImpl(system.settings().config());
	}
}