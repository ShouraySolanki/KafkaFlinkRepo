/*
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

import java.util.Properties


class FlinkProduce {


  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val properties = new Properties
  properties.setProperty("bootstrap.servers", "localhost:9092")
  def produce(){

    val stream = env.
    val myProducer = new FlinkKafkaProducer[String](
      "jsontest1",                  // target topic
      new SimpleStringSchema(),    // serialization schema
      properties) // fault-tolerance


    stream.addSink(myProducer)
  }


}
*/
