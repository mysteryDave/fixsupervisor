package fixsupervisor

import fixsupervisor.model.{TradeEventKey, TradeEventValues}
import org.apache.kafka.streams.processor.{Processor, ProcessorContext, ProcessorSupplier}
import org.slf4j.{Logger, LoggerFactory}

class RaiseAlert extends Processor[(String, TradeEventKey, TradeEventValues), TradeEventValues]
  with ProcessorSupplier[(String, TradeEventKey, TradeEventValues), TradeEventValues] {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private var context: ProcessorContext = null
  override def init(context: ProcessorContext): Unit = { this.context = context }

  override def process(key: (String, TradeEventKey, TradeEventValues), value: TradeEventValues): Unit =
    logger.warn("ALERT: LIMIT BREACHED {}\nLimit:{}\nLimit:{}\nActual:{}", key._1, key._2, key._3, value)

  override def get(): Processor[(String, TradeEventKey, TradeEventValues), TradeEventValues] = this

  override def close(): Unit = {}
}