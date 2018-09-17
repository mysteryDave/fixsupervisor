package fixsupervisor

import fixsupervisor.model.{TradeEventKey, TradeEventValues}
import org.apache.kafka.streams.KeyValue

/**
  * Handles translating the serialised FIX string into a meaning ful set of dimensions(key) and facts(data) for supervision.
  * TO DO: Replace with lookups or generated code that can reference QuickFIX XML dictionary and or configuration files.
  */
object FixUtil {
  class KeyBuilder(var account: String = "",  //1
                   var currency: String = "",  //15
                   var exchange: String = "", //30
                   var state:String = "",      //39
                   var capacity: String = "",  //47
                   var client: String = "",    //49
                   var side: String = "",      //54
                   var venue: String = "",     //57
                   var timeInForce: String = "") { //59
    def build: TradeEventKey = new TradeEventKey(account=account, capacity=capacity, currency=currency, state=state, client=client, side=side, venue=venue, timeInForce=timeInForce)
  }

  class ValueBuilder(var cumulativeQty: Long = 0, //14
                     var cumulativeValue: Double = 0.0,  //6 * 44
                     var lastQty: Long = 0, //31
                     var lastValue: Double = 0.0, //31 * 32
                     var orderQty: Long = 0, //38
                     var orderValue: Double = 0.0,  //38 * 44
                     var leavesQty: Long = 0, //151
                     var leavesValue: Double = 0.0) { //151 * 44

    def build: TradeEventValues = new TradeEventValues(cumulativeQty=cumulativeQty, cumulativeValue=cumulativeValue, lastQty=lastQty, lastValue=lastValue, orderQty=orderQty, orderValue=orderValue)
  }

  def parse(fixmsg: String): KeyValue[TradeEventKey, TradeEventValues] = {
    val keyBldr = new KeyBuilder()
    val valBldr = new ValueBuilder()

    fixmsg.split(1.toChar).toStream.map(pair => pair.split("=")).foreach(pair => {
      val(tag: Int, value: String) = (pair(0).toInt, pair(1))
      tag match {
        case 1 => keyBldr.account = value
        case 6 => valBldr.cumulativeValue = value.toDouble
        case 14 => valBldr.cumulativeQty = value.toLong
        case 31 => valBldr.lastQty = value.toLong
        case 32 => valBldr.lastValue = value.toDouble
        case 38 => valBldr.orderQty = value.toLong
        case 39 => keyBldr.state = value
        case 44 => valBldr.orderValue = value.toDouble
        case 47 => keyBldr.capacity = value match {
          case "A" => "Agency"
          case "P" => "Principal"
          case _ => ""
        }
        case 49 => keyBldr.client = value
        case 54 => keyBldr.side = value match {
          case "1"|"5" => "Buy"
          case "2"|"3" => "Sell"
          case _ => ""
        }
        case 57 => keyBldr.venue = value
        case 59 => keyBldr.timeInForce = value match {
          case "1" => "GTC"
          case "2" => "OPG"
          case "3" => "IOC"
          case "4" => "FOK"
          case "5" => "GTX"
          case "6" => "GTD"
          case _ => "DAY"
        }
        case 151 => valBldr.leavesQty = value.toLong
      }
    })

    new KeyValue(keyBldr.build, valBldr.build)
  }
}
