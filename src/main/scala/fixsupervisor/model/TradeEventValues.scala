package fixsupervisor.model

class TradeEventValues(val cumulativeQty: Long = 0, //14
                       val cumulativeValue: Double = 0.0,  //6 * 44
                       val lastQty: Long = 0, //31
                       val lastValue: Double = 0.0, //31 * 32
                       val orderQty: Long = 0, //38
                       val orderValue: Double = 0.0, //38 * 44
                       val leavesQty: Long = 0, //151
                       val leavesValue: Double = 0.0) { //151 * 44
  override def toString: String = s"6=$cumulativeValue|14=$cumulativeQty|31=$lastQty|32=$lastValue|38=$orderQty|44=$orderValue|151=$leavesQty|leavesValue=$leavesValue"
}