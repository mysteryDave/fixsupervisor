package fixsupervisor

import fixsupervisor.model.{TradeEventKey, TradeEventValues}
import org.apache.kafka.streams.processor.{Processor, ProcessorContext, ProcessorSupplier}
import org.slf4j.{Logger, LoggerFactory}

class RaiseAlert extends Processor[TradeEventKey, TradeEventValues]
  with ProcessorSupplier[TradeEventKey, TradeEventValues] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private var context: ProcessorContext = null
  override def init(context: ProcessorContext): Unit = { this.context = context }

  override def process(key: TradeEventKey, value: TradeEventValues): Unit = {
    logger.warn("ALERT: LIMIT BREACHED Condition:{}", key.toString)
    logger.warn("ALERT: LIMIT BREACHED Actuals:{}", value.toString)

  }

  override def get(): Processor[TradeEventKey, TradeEventValues] = this

  override def close(): Unit = {}
}