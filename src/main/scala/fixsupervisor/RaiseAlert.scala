package fixsupervisor

import fixsupervisor.model.{TradeEventKey, TradeEventValues}
import org.apache.kafka.streams.processor.{Processor, ProcessorContext}

class LimitChecker extends Processor[TradeEventKey, TradeEventValues]{
  private var context: ProcessorContext = null
  override def init(context: ProcessorContext): Unit = { this.context = context }

  override def process(key: TradeEventKey, value: TradeEventValues): Unit = ???

  override def close(): Unit = {}
}