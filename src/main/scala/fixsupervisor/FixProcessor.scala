package fixsupervisor

import java.util.Properties

import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.processor.{Processor, ProcessorContext, ProcessorSupplier, StreamPartitioner}
import org.apache.kafka.common.serialization.{Serde, StringSerializer}
import org.apache.kafka.streams.KeyValue

object FixProcessor {

  def main(args: Array[String]): Unit = {

    val config = {
      val properties = new Properties()

      properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "aggregator")
      properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
      properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
      properties.put(StreamsConfig.STATE_DIR_CONFIG, "E:/OLDEN/Den_old/Documents/David/Uni/Birkbeck/PROJECT/kafka/streams")

      properties
    }

    val builder = new StreamsBuilder

    val sourceStream = builder.stream("FixMessages")
    sourceStream.to("FIXdup")

    val streams: KafkaStreams = new KafkaStreams(builder.build, config)
    val shutDownHook = new streamShutdown(streams)
    streams.start()
    Runtime.getRuntime.addShutdownHook(shutDownHook)
  }

}

class streamShutdown(streams: KafkaStreams) extends Thread {
  override def run() { streams.close() }
}