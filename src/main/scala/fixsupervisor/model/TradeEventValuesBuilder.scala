package fixsupervisor.model

class TradeEventValuesBuilder(var cumulativeQty: Long = 0, //14
                              var cumulativeValue: Double = 0.0, //6 * 44
                              var lastQty: Long = 0, //31
                              var lastValue: Double = 0.0, //31 * 32
                              var orderQty: Long = 0, //38
                              var orderValue: Double = 0.0, //38 * 44
                              var leavesQty: Long = 0, //151
                              var leavesValue: Double = 0.0) { //151 * 44
  def build(): TradeEventValues = new TradeEventValues(cumulativeQty, cumulativeValue, lastQty, lastValue, orderQty, orderValue, leavesQty, leavesValue)
}