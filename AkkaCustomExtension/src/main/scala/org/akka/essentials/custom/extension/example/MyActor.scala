package org.akka.essentials.custom.extension.example
import akka.actor.Actor

class MyActor extends Actor {
  def receive: Receive = {
    case _ =>
      val mysqlSetting = MySQLJDBCSettings(context.system)
      println(mysqlSetting.DB_USER_NAME)
      println(mysqlSetting.DB_USER_PASSWORD)
  }
}