package fixsupervisor.model

class TradeEventKey( val account: String = "", //1
                     val currency: String = "", //15
                     val market: String = "", //30
                     val state:String = "", //39
                     val capacity: String = "", //47
                     val venue: String = "", //49
                     val side: String = "", //54
                     val client: String = "", //56
                     val timeInForce: String = "", //57
                     val exchange: String = "", //65
                     val exDest: String = "" ) { //100
 override def toString: String = s"1=$account|15=$currency|30=$market|39=$state|47=$capacity|49=$client|54=$side|57=$venue|59=$timeInForce|65=$exchange|100=$exDest"
}