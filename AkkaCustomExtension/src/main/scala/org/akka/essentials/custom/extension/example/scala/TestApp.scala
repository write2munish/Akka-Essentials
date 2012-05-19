package org.akka.essentials.custom.extension.example.scala
import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem

object TestApp {

  def main(args: Array[String]): Unit = {}
  val system = ActorSystem("Extension-Test", ConfigFactory.load()
    .getConfig("TestApp"))

  val mysqlSetting = MySQLJDBCSettings(system)

  System.out.println(mysqlSetting.DB_NAME);
  System.out.println(mysqlSetting.DB_URL);

}