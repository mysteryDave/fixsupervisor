package fixsupervisor

import java.util.Properties

import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.processor.{Processor, ProcessorContext, ProcessorSupplier, StreamPartitioner}
import org.apache.kafka.common.serialization.{Serde, StringSerializer}
import org.apache.kafka.streams.KeyValue

object StreamApplication {

  def main(args: Array[String]): Unit = {

    val config = {
      val properties = new Properties()

      properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "aggregator")
      properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
      properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)

      properties
    }

    val builder = new StreamsBuilder

    val sourceStream = builder.stream("FIX")
    sourceStream.to("FIXdup")

    val streams: KafkaStreams = new KafkaStreams(builder.build, config)
    streams.start()

  }

}

class FixProcessor {

}
