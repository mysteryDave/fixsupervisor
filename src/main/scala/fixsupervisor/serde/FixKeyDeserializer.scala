package fixsupervisor.serde

import java.util

import fixsupervisor.model.{TradeEventKey, TradeEventKeyBuilder}
import org.apache.kafka.common.serialization.Deserializer

class FixKeyDeserializer extends Deserializer[TradeEventKey] {

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): TradeEventKey = {
    val keys: TradeEventKeyBuilder = new TradeEventKeyBuilder
    new String(data).split(1.toChar).toStream.map(pair => pair.split("=")).foreach(pair => {
      val (tag: Int, value: String) = (pair(0).toInt, pair(1))
      tag match {
        case 1 => keys.account = value
        case 15 => keys.currency = value
        case 39 => keys.state = value
        case 47 => keys.capacity = value
        case 49 => keys.venue = value
        case 54 => keys.side = value
        case 56 => keys.client = value
        case 59 => keys.timeInForce = value
      }
    })
    keys.build()
  }

  override def close(): Unit = {}
}
