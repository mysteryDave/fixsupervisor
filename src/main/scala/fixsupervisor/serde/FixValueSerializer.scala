package fixsupervisor.serde

import java.util

import fixsupervisor.model.TradeEventValues
import org.apache.kafka.common.serialization.Serializer

class FixValueSerializer extends Serializer[TradeEventValues] {
  val SOH: Char = 1.toChar

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def serialize(topic: String, data: TradeEventValues): Array[Byte] = {
    println(s"SERDE serialising value $data")
    var fixString: String = ""
    if(data.cumulativeValue != null && data.cumulativeValue >= 0) fixString=s"$fixString${SOH}6=${data.cumulativeValue}"
    if(data.cumulativeQty != null && data.cumulativeQty >= 0) fixString=s"$fixString${SOH}14=${data.cumulativeQty}"
    if(data.lastQty != null && data.lastQty >= 0) fixString=s"$fixString${SOH}31=${data.lastQty}"
    if(data.lastValue != null && data.lastValue >= 0) fixString=s"$fixString${SOH}32=${data.lastValue}"
    if(data.orderQty != null && data.orderQty >= 0) fixString=s"$fixString${SOH}38=${data.orderQty}"
    if(data.orderValue != null && data.orderValue >= 0) fixString=s"$fixString${SOH}44=${data.orderValue}"
    if(data.leavesQty != null && data.leavesQty >= 0) fixString=s"$fixString${SOH}151=${data.leavesQty}"
    if(data.leavesValue != null && data.leavesValue >= 0) fixString=s"$fixString${SOH}154=${data.leavesValue}" //Not a real FIX tag
    val finalFix = fixString.toString()
    println(s"SERDE serialised value $finalFix")
    if (finalFix.length > 1) finalFix.substring(0, finalFix.length - 1).getBytes("UTF-8") //Assumes it was not an empty object!
    else "EMPTY".getBytes("UTF-8")
  }

  override def close(): Unit = {}
}
