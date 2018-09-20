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
 def matches(limit: TradeEventKey): Boolean =
     (limit.account.length <= 0 || this.account.equals(limit.account)) &&
     (limit.currency.length <= 0 || this.currency.equals(limit.currency)) &&
     (limit.market.length <= 0 || this.market.equals(limit.market)) &&
     (limit.state.length <= 0 || this.state.equals(limit.state)) &&
     (limit.capacity.length <= 0 || this.capacity.equals(limit.capacity)) &&
     (limit.venue.length <= 0 || this.venue.equals(limit.venue)) &&
     (limit.side.length <= 0 || this.side.equals(limit.side)) &&
     (limit.client.length <= 0 || this.client.equals(limit.client)) &&
     (limit.timeInForce.length <= 0 || this.timeInForce.equals(limit.timeInForce)) &&
     (limit.exchange.length <= 0 || this.exchange.equals(limit.exchange)) &&
     (limit.exDest.length <= 0 || this.exDest.equals(limit.exDest))
}