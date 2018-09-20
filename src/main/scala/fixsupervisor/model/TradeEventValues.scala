package fixsupervisor.model

class TradeEventValues( val count: Long = 1, //message count
                        val cumulativeQty: Long = 0, //14
                        val cumulativeValue: Double = 0.0,  //6 * 44
                        val lastQty: Long = 0, //31
                        val lastValue: Double = 0.0, //31 * 32
                        val orderQty: Long = 0, //38
                        val orderValue: Double = 0.0, //38 * 44
                        val leavesQty: Long = 0, //151
                        val leavesValue: Double = 0.0) { //151 * 44
  override def toString: String = s"6=$cumulativeValue|14=$cumulativeQty|31=$lastQty|32=$lastValue|38=$orderQty|44=$orderValue|151=$leavesQty|leavesValue=$leavesValue"
  def +(that: TradeEventValues): TradeEventValues = new TradeEventValues(this.count + that.count,
    this.cumulativeQty + that.cumulativeQty, this.cumulativeValue + that.cumulativeValue,
    this.lastQty + that.lastQty, this.lastValue + that.lastValue,
    this.orderQty + that.orderQty, this.orderValue + that.orderValue,
    this.leavesQty + that.leavesQty, this.leavesValue + that.leavesValue)
  def exceeds(limit: TradeEventValues) : Boolean =
    (limit.count>0 && this.count > limit.count) ||
      (limit.cumulativeQty >0 && this.cumulativeQty > limit.cumulativeQty) ||
      (limit.cumulativeValue >0 && this.cumulativeValue > limit.cumulativeValue) ||
      (limit.lastQty >0 && this.lastQty > limit.lastQty) ||
      (limit.lastValue >0 && this.lastValue > limit.lastValue) ||
      (limit.orderQty >0 && this.orderQty > limit.orderQty) ||
      (limit.orderValue >0 && this.orderValue > limit.orderValue) ||
      (limit.leavesQty >0 && this.leavesQty > limit.leavesQty) ||
      (limit.leavesValue >0 && this.leavesValue > limit.leavesValue)
}