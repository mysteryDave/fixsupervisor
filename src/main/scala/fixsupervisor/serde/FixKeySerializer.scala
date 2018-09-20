package fixsupervisor.serde

import java.util

import fixsupervisor.model.TradeEventKey
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.{Logger, LoggerFactory}

class FixKeySerializer extends Serializer[TradeEventKey] {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  val SOH: Char = 1.toChar

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def serialize(topic: String, data: TradeEventKey): Array[Byte] = {
    logger.debug(s"SERDE serialising key $data")
    var fixString: String = ""
    if(data.account != null && data.account.length > 0) fixString=s"$fixString${SOH}1=${data.account}"
    if(data.currency != null && data.currency.length > 0) fixString=s"$fixString${SOH}15=${data.currency}"
    if(data.state != null && data.state.length > 0) fixString=s"$fixString${SOH}39=${data.state}"
    if(data.capacity != null && data.capacity.length > 0) fixString=s"$fixString${SOH}47=${data.capacity}"
    if(data.venue != null && data.venue.length > 0) fixString=s"$fixString${SOH}49=${data.venue}"
    if(data.side != null && data.side.length > 0) fixString=s"$fixString${SOH}54=${data.side}"
    if(data.client != null && data.client.length > 0) fixString=s"$fixString${SOH}56=${data.client}"
    if(data.timeInForce != null && data.timeInForce.length > 0) fixString=s"$fixString${SOH}57=${data.timeInForce}"
    val finalFix = fixString.toString()
    logger.debug(s"SERDE serialised key $finalFix")
    if (finalFix.length > 1) finalFix.substring(1).getBytes("UTF-8") //Assumes it was not an empty object!
    else "EMPTY".getBytes("UTF-8")
  }

  override def close(): Unit = {}
}
