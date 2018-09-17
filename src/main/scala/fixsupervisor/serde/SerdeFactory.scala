package fixsupervisor.serde

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serdes, Serializer}

class serdeFactory[T](isKey: Boolean = false) {
  val serdeProps: java.util.Map[String, Object] = new java.util.HashMap[String, Object]()
  val classProperty: String = "JsonPOJOClass"

  def getSerde: Serde[T] = Serdes.serdeFrom(getSerializer, getDeserializer)

  private def getSerializer: Serializer[T] = {
    val theSerializer: Serializer[T] = new JsonPOJOSerializer[T]
    serdeProps.put(classProperty, classOf[JsonPOJOSerializer[T]])
    theSerializer.configure(serdeProps, isKey)
    theSerializer
  }

  private def getDeserializer: Deserializer[T] = {
    val theDeserializer: Deserializer[T] = new JsonPOJODeserializer[T]
    serdeProps.put(classProperty, classOf[JsonPOJODeserializer[T]])
    theDeserializer.configure(serdeProps, true)
    theDeserializer
  }
}