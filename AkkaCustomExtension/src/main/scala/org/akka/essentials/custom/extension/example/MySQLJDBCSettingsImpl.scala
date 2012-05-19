package org.akka.essentials.custom.extension.example
import akka.actor.Extension
import com.typesafe.config.Config

class MySQLJDBCSettingsImpl(config: Config) extends Extension {
  val DB_URL: String = config.getString("connection.db.mysql.url")
  val DB_NAME: String = config.getString("connection.db.mysql.dbname")
  val DB_DRIVER: String = config.getString("connection.db.mysql.driver")
  val DB_USER_NAME: String = config.getString("connection.db.mysql.username")
  val DB_USER_PASSWORD: String = config.getString("connection.db.mysql.userpassword")
}	