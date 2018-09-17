package fixsupervisor

import java.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import fixsupervisor.model.TradeEventKey
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.{Logger, LoggerFactory}

class SupervisionKeySerializer extends Serializer[TradeEventKey]{
  val logger: Logger = LoggerFactory.getLogger(SupervisionKeySerializer.getClass)

  def serialize(topic: String, data: TradeEventKey): Array[Byte] = {
    val writer = new ObjectMapper().writer
    var jsonBytes = new Array[Byte](0)
    try
      jsonBytes = writer.writeValueAsString(data).getBytes
    catch {
      case e: JsonProcessingException =>
        logger.error("Failed to serialize object", e)
    }
    jsonBytes
  }

  def close(): Unit = {}

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}
}

object SupervisionKeySerializer {}

