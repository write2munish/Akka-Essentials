package org.akka.essentials.serializer
import com.google.gson.GsonBuilder
import akka.serialization.Serializer

class MySerializer extends Serializer {
  def identifier = 12062010
  def includeManifest: Boolean = true
  val gson = new GsonBuilder().serializeNulls().create()

  // "toBinary" serializes the given object to an Array of Bytes
  def toBinary(obj: AnyRef): Array[Byte] = {
    gson.toJson(obj).getBytes()
  }
  // "fromBinary" deserializes the given array,
  // using the type hint (if any, see "includeManifest" above)
  // into the optionally provided classLoader.
  def fromBinary(bytes: Array[Byte],
    clazz: Option[Class[_]]): AnyRef = {
    gson.fromJson(new String(bytes), clazz.toList.first)
  }
}