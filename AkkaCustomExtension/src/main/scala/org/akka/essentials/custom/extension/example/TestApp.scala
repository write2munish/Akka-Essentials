package org.akka.essentials.custom.extension.example

import com.typesafe.config.ConfigFactory

import akka.actor.actorRef2Scala
import akka.actor.ActorSystem
import akka.actor.Props

object TestApp {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Extension-Test", ConfigFactory.load()
      .getConfig("TestApp"))

    val ref = system.actorOf(Props[MyActor])
    ref ! "print"

    val mysqlSetting = MySQLJDBCSettings(system)

    println(mysqlSetting.DB_NAME)
    println(mysqlSetting.DB_URL)
  }

}