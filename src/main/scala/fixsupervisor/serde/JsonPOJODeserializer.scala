package fixsupervisor.serde

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer

class JsonPOJODeserializer[T]() extends Deserializer[T] {
  private val objectMapper = new ObjectMapper
  private var tClass: Class[T] = null

  @SuppressWarnings(Array("unchecked")) override def configure(props: util.Map[String, _], isKey: Boolean): Unit = {
    tClass = props.get("JsonPOJOClass").asInstanceOf[Class[T]]
  }

  override def deserialize(topic: String, bytes: Array[Byte]): T = objectMapper.readValue(bytes, tClass)

  override def close(): Unit = {
  }
}
