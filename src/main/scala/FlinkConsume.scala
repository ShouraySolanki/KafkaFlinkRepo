import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods

import java.util.Properties


object FlinkConsume {


  def main(args: Array[String]) {
    implicit lazy val formats: DefaultFormats.type = org.json4s.DefaultFormats
    val flinkProduceFunction = new FlinkProduceFunction
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("zookeeper.connect", "localhost:2181")
    properties.setProperty("group.id", "consumerGroup")
    val stream = env.addSource(new FlinkKafkaConsumer[String]("jsontest", new SimpleStringSchema(), properties))

    val jsonStream = stream.flatMap(raw => JsonMethods.parse(raw).toOption).map(_.extract[Sum])
    val ingestStream = stream

      .process(flinkProduceFunction)

    val a = jsonStream.map(value=> value.a)
    val b = jsonStream.map(value=> value.b)
    val typ = jsonStream.map(value=> value.typ)
      a.print()
      b.print()




        val mySumProducer = new FlinkKafkaProducer[String](
          "jsontest1",
          new SimpleStringSchema(),
          properties)
        ingestStream.getSideOutput( flinkProduceFunction.sumOutputTag).addSink(mySumProducer)

    val myAverageProducer = new FlinkKafkaProducer[String](
      "jsontest2",
      new SimpleStringSchema(),
      properties)
    ingestStream.getSideOutput( flinkProduceFunction.averageOutputTag).addSink(myAverageProducer)


    /*  val myProducer = new FlinkKafkaProducer[String](
        "jsontest2",
        new SimpleStringSchema(),
        properties)
      ingestStream.addSink(myProducer)
    */


    //    val newSum:DataStream[Int] = jsonStream.map(value=> value.a + value.b)
    //print(newSum)

//    val message = write(jsonStream)
//    val messageGenerator = new MessageGenerator
//    messageGenerator.genarateMessage(message)


    env.execute()
  }
}