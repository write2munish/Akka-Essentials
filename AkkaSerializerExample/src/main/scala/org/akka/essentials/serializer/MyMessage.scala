package org.akka.essentials.serializer

class MyMessage(private val name: String, private val age: Int, private val city: String) {

  def getCity: String = {
    return city
  }

  def getName: String = {
    return name
  }

  def getAge: Int = {
    return age
  }

  override def toString: String = {
    return name + "," + age + "," + city
  }

}