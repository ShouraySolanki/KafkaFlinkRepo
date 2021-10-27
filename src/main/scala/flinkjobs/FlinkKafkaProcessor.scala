package flinkjobs

import configurations.FlinkSampleConfiguration
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.json4s.DefaultFormats

import java.util.Properties

class FlinkKafkaProcessor(config: FlinkSampleConfiguration) {


  def process(): Unit = {
    implicit lazy val formats: DefaultFormats.type = org.json4s.DefaultFormats
    val flinkProduceFunction = new FlinkProcessFunction
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("zookeeper.connect", "localhost:2181")
    properties.setProperty("group.id", "consumerGroup")
    val stream = env.addSource(new FlinkKafkaConsumer[String]("jsontest", new SimpleStringSchema(), properties))

    //val jsonStream = stream.flatMap(raw => JsonMethods.parse(raw).toOption).map(_.extract[Sum])
    val ingestStream = stream

      .process(flinkProduceFunction)
/*
    val a = jsonStream.map(value => value.a)
    val b = jsonStream.map(value => value.b)

    a.print()
    b.print()*/


    val mySumProducer = new FlinkKafkaProducer[String](
      config.jsontest1,
      new SimpleStringSchema(),
      properties)
    ingestStream.getSideOutput(flinkProduceFunction.sumOutputTag).addSink(mySumProducer)

    val myAverageProducer = new FlinkKafkaProducer[String](
      "jsontest2",
      new SimpleStringSchema(),
      properties)
    ingestStream.getSideOutput(flinkProduceFunction.averageOutputTag).addSink(myAverageProducer)


    env.execute()
  }
}
