package fixsupervisor

import java.util.Properties
import java.util.concurrent.TimeUnit

import fixsupervisor.model.{TradeEventKey, TradeEventValues}
import fixsupervisor.serde._
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.processor.{Processor, ProcessorSupplier}
import org.slf4j.{Logger, LoggerFactory}

object FixProcessor {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
//TODO replace hardcoded example trading limits with CSV file or better a limit stream
  val tradingLimits: List[Tuple3[String, TradeEventKey, TradeEventValues]] = List(
    ("No more than £10m of open orders in Sterling.", new TradeEventKey(currency="GBP", state="OPEN"), new TradeEventValues(count = 0, leavesQty = 10000000)),
    ("No more than 10 orders in EUR, no more than 50M EUR executed.", new TradeEventKey(currency="EUR"), new TradeEventValues(count = 10, cumulativeValue = 50000000))
  )

  def main(args: Array[String]): Unit = {
    // Source stream of FIX Bytes from FIX engine. Transform to meaningful keys and values.
    val builder = new StreamsBuilder
    val sourceStream: KStream[String, String] = builder.stream("FixEventsIn")
    val transformedStream: KStream[TradeEventKey, TradeEventValues] = sourceStream.map((_, value) => FixUtil.parse(value): KeyValue[TradeEventKey, TradeEventValues])
    val initializer: Initializer[TradeEventValues] = () => new TradeEventValues(count = 0)
    val aggregator: Aggregator[Any, TradeEventValues, TradeEventValues] = (key: Any, value: TradeEventValues, aggregate: TradeEventValues) => aggregate + value
    val snapshot: KTable[TradeEventKey, TradeEventValues] = transformedStream.groupBy((key,_) => key)
      .aggregate(initializer, aggregator
        ,Materialized.as("TradingSnapshot"))
    //Push the transformed and aggregated flows downstream for other applications to connect to.
    //"Serde" refers to SERialiser/DEserializer
    val keySerde: Serde[TradeEventKey] = Serdes.serdeFrom(new FixKeySerializer, new FixKeyDeserializer)
    val valueSerde: Serde[TradeEventValues] = Serdes.serdeFrom(new FixValueSerializer, new FixValueDeserializer)
    transformedStream.to("TradeEvents", Produced.`with`(keySerde,valueSerde))
    snapshot.toStream.to("TradeTotals", Produced.`with`(keySerde,valueSerde))

    val soundAlarm: ProcessorSupplier[(String, TradeEventKey, TradeEventValues), TradeEventValues] = new RaiseAlert
    for (limit <- tradingLimits) snapshot.toStream
        .filter((key: TradeEventKey, _) => key.matches(limit._2: TradeEventKey)) //filter to match alert
        .map((_, value: TradeEventValues) => new KeyValue[TradeEventKey, TradeEventValues](limit._2, value: TradeEventValues)) //remove key
        .groupByKey()
        .aggregate(initializer, aggregator) //sum all components that match
        .filter((_, value: TradeEventValues) => value.exceeds(limit._3: TradeEventValues)) //filter to those breaching limit
        .toStream
        .map((_, value) => new KeyValue(limit: (String, TradeEventKey, TradeEventValues), value: TradeEventValues))//preserve limit detail
        .process(soundAlarm) //alert users to limit breach
        //.foreach((key,value) => soundAlarm.process(key, value)) //alert users to limit breach

    //Run stream flow until term called to shut down
    val streamTopology = builder.build()
    logger.info(streamTopology.describe().toString)
    val streams: KafkaStreams = new KafkaStreams(streamTopology, config)
    val shutDownHook = new streamShutdown(streams)
    streams.start()
    Runtime.getRuntime.addShutdownHook(shutDownHook)
  }

  def config: Properties = {
    val properties = new Properties()
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "TradeSupervisor")
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties.put(StreamsConfig.STATE_DIR_CONFIG, "E:/OLDEN/Den_old/Documents/David/Uni/Birkbeck/PROJECT/kafka/streams")
    properties
  }

  class streamShutdown(streams: KafkaStreams) extends Thread {
    override def run(): Unit = streams.close(10, TimeUnit.SECONDS)
  }

}