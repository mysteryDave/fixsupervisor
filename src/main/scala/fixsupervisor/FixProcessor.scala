package fixsupervisor

import java.util.Properties

import fixsupervisor.model.{TradeEventKey, TradeEventValues}
import fixsupervisor.serde._
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.KeyValue

object FixProcessor {

  def main(args: Array[String]): Unit = {

    /**
      * Setup stream flow.
      * "Serde" refers to SERialiser/DEserializer
      */
    val builder = new StreamsBuilder
    val sourceStream: KStream[String, String] = builder.stream("FixEventsIn")
    val transformedStream: KStream[TradeEventKey, TradeEventValues] = sourceStream.map((_, value) => FixUtil.parse(value): KeyValue[TradeEventKey, TradeEventValues])

    val keySerde: Serde[TradeEventKey] = Serdes.serdeFrom(new FixKeySerializer, new FixKeyDeserializer)
    val valueSerde: Serde[TradeEventValues] = Serdes.serdeFrom(new FixValueSerializer, new FixValueDeserializer)
    val grouped: KGroupedStream[TradeEventKey, TradeEventValues] = transformedStream.groupBy((key, value) => key),keySerde, valueSerde)
    val snapshot: KTable[TradeEventKey, java.lang.Long] = grouped.count()
    transformedStream.to("TradeEvents", Produced.`with`(keySerde,valueSerde))

    /**
      * Run stream flow until term called to shut down
      */
    val streamTopology = builder.build()
    println(streamTopology.describe())
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
    override def run(): Unit = streams.close()
  }

}