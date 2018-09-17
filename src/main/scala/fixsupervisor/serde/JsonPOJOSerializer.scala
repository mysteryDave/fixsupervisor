package fixsupervisor.serde

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer

/**
  * Adapted from Java code
  * @link https://github.com/apache/kafka/blob/1.0/streams/examples/src/main/java/org/apache/kafka/streams/examples/pageview/JsonPOJOSerializer.java
  * to facilitate JSON (human readable serialization)
  */
class JsonPOJOSerializer[T]() extends Serializer[T] {
    final private val objectMapper = new ObjectMapper

    override def configure(props: util.Map[String, _], isKey: Boolean): Unit = {
    }

    override def serialize(topic: String, data: T): Array[Byte] = {
      if (data == null) return null
      try
        objectMapper.writeValueAsBytes(data)
      catch {
        case e: Exception =>
          throw new SerializationException("Error serializing JSON message", e)
      }
    }

    override def close(): Unit = {
    }
  }
