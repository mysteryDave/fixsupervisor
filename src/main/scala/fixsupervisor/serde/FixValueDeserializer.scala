package fixsupervisor.serde

import java.util

import fixsupervisor.model.{TradeEventValues, TradeEventValuesBuilder}
import org.apache.kafka.common.serialization.Deserializer

class FixValueDeserializer extends Deserializer[TradeEventValues] {
  val SOH: Char = 1.toChar

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): TradeEventValues = {
    val values: TradeEventValuesBuilder = new TradeEventValuesBuilder
    new String(data).split(1.toChar).toStream.map(pair => pair.split("=")).foreach(pair => {
      val (tag: Int, value: String) = (pair(0).toInt, pair(1))
      tag match {
        case 6 => values.cumulativeValue = value.toDouble
        case 14 => values.cumulativeQty = value.toLong
        case 31 => values.lastQty = value.toLong
        case 32 => values.lastValue = value.toDouble
        case 38 => values.orderQty = value.toLong
        case 44 => values.orderValue = value.toDouble
        case 151 => values.leavesQty = value.toLong
        case 154 => values.leavesValue = value.toDouble
      }
    })
    values.build()
  }

  override def close(): Unit = {}
}
