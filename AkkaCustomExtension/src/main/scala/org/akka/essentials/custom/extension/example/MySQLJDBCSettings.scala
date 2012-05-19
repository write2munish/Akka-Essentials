package org.akka.essentials.custom.extension.example
import akka.actor.ExtensionIdProvider
import akka.actor.ExtensionId
import akka.actor.ExtendedActorSystem

object MySQLJDBCSettings extends ExtensionId[MySQLJDBCSettingsImpl] with ExtensionIdProvider {

  override def lookup = MySQLJDBCSettings

  override def createExtension(system: ExtendedActorSystem) = new MySQLJDBCSettingsImpl(system.settings.config)
}