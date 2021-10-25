import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods

import java.util.Properties


object FlinkConsume {


  def main(args: Array[String]) {
    implicit lazy val formats: DefaultFormats.type = org.json4s.DefaultFormats
    val outputTag = OutputTag[String]("side-output")
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("zookeeper.connect", "localhost:2181")
    properties.setProperty("group.id", "consumerGroup")
    val stream = env.addSource(new FlinkKafkaConsumer[String]("jsontest", new SimpleStringSchema(), properties))

    val jsonStream = stream.flatMap(raw => JsonMethods.parse(raw).toOption).map(_.extract[Sum])

    //jsonStream.addSink(Sum=> s)
    val a = jsonStream.map(value=> value.a)
    val b = jsonStream.map(value=> value.b)
      a.print()
      b.print()
    val newSum:DataStream[Int] = jsonStream.map(value=> value.a + value.b)
    print(newSum)

//    val message = write(jsonStream)
//    val messageGenerator = new MessageGenerator
//    messageGenerator.genarateMessage(message)


    env.execute()
  }
}