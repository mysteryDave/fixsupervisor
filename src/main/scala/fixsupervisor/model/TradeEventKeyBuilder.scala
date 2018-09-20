package fixsupervisor.model

class TradeEventKeyBuilder( var account: String = "", //1
                            var currency: String = "", //15
                            var market: String = "", //30
                            var state:String = "", //39
                            var capacity: String = "", //47
                            var venue: String = "", //49
                            var side: String = "", //54
                            var client: String = "", //56
                            var timeInForce: String = "", //57
                            var exchange: String = "", //65
                            var exDest: String = "" ) { //100
  def build(): TradeEventKey = new TradeEventKey(account, currency, market, state, capacity, venue, side, client, timeInForce, exchange, exDest)
 }