package fixsupervisor

import fixsupervisor.model._
import org.apache.kafka.streams.KeyValue

/**
  * Handles translating the serialised FIX string into a meaningful set of dimensions(key) and facts(data) for supervision.
  * TO DO: Replace with lookups or generated code that can reference QuickFIX XML dictionary and or configuration files.
  */
object FixUtil {
  def parse(fixmsg: String): KeyValue[TradeEventKey, TradeEventValues] = {
    println(s"parsing $fixmsg")
    val keyBldr = new TradeEventKeyBuilder()
    val valBldr = new TradeEventValuesBuilder()

    fixmsg.split(1.toChar).toStream.map(pair => pair.split("=")).foreach(pair => {
      val(tag: Int, value: String) = (pair(0).toInt, pair(1))
      println(s"  parsing pair $tag=$value")
      tag match {
        case 1 => keyBldr.account = value
        case 6 => valBldr.cumulativeValue = value.toDouble
        case 14 => valBldr.cumulativeQty = toLong(value)
        case 30 => keyBldr.market = value
        case 31 => valBldr.lastQty = toLong(value)
        case 32 => valBldr.lastValue = value.toDouble
        case 38 => valBldr.orderQty = toLong(value)
        case 39 => keyBldr.state = value match {
          case "A"|"E" => "PENDING"
          case "0"|"1"|"5"|"6"|"D" => "OPEN"
          case "2" => "EXECUTED"
          case "3"|"B" => "DONE"
          case "4"|"7"|"9"|"C" => "CLOSED"
        }
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
        case 65 => keyBldr.exchange = value
        case 100 => keyBldr.exDest = value
        case 151 => valBldr.leavesQty = toLong(value)
        case _ => //do nothing
      }
    })

    println(s"KEY:$keyBldr")
    println(s"VALUE:$valBldr")
    new KeyValue(keyBldr.build, valBldr.build)
  }

  //Ignore decimal points for quantity fields
  def toLong(value: String): Long = {
    val transStr: String = if (value.contains('.')) value.substring(0,value.indexOf('.'))
    else value
    transStr.toLong
  }
}
