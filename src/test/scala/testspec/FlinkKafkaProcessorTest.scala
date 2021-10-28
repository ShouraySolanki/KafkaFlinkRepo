package testspec


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.typesafe.config.{Config, ConfigFactory}
import configurations.FlinkSampleConfiguration
import flinkjobs.FlinkKafkaProcessor
import jsondata.TestData
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.test.util.{MiniClusterResourceConfiguration, MiniClusterWithClientResource}
import org.mockito.Mockito
import org.mockito.Mockito.when
import org.mockito.MockitoSugar.mock
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import util.{FlinkKafkaConnector, Sum}

import java.io.ByteArrayOutputStream
import java.util

class FlinkKafkaProcessorTest extends AnyFlatSpec with Matchers {

  val flinkCluster = new MiniClusterWithClientResource(new MiniClusterResourceConfiguration.Builder()
    .setNumberSlotsPerTaskManager(1)
    .setNumberTaskManagers(1)
    .build)

  val config: Config = ConfigFactory.load("application.conf").getConfig("com.ram.batch")
  val flinkSampleConfiguration = new FlinkSampleConfiguration(config)

  val mockKafkaConnector: FlinkKafkaConnector = mock[FlinkKafkaConnector](Mockito.withSettings().serializable())


    when(mockKafkaConnector.kafkaConsumer(flinkSampleConfiguration.jsontest))thenReturn(new FlinkSampleEventSource)

    when(mockKafkaConnector.kafkaProducer(flinkSampleConfiguration.jsontest1))thenReturn(new SumDataSink)

    when(mockKafkaConnector.kafkaProducer(flinkSampleConfiguration.jsontest2))thenReturn(new AverageDataSink)

  "Flink Kafka Processor " should "process the data" in{

    val task = new FlinkKafkaProcessor(flinkSampleConfiguration, mockKafkaConnector)
    task.process()

    SumDataSink.values.size() should be(1)
    //AverageDataSink.values.size() should be(1)


  }



}

class FlinkSampleEventSource extends SourceFunction[String]{
  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {

    //val gson = new Gson()
    val objectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)

    val data1: Sum = objectMapper.readValue[Sum](TestData.Data_1)
    val testData1 = new ByteArrayOutputStream()
    objectMapper.writeValue(testData1, data1)

    val data2: Sum = objectMapper.readValue[Sum](TestData.Data_2)
    val testData2 = new ByteArrayOutputStream()
    objectMapper.writeValue(testData2, data2)

    val data3: Sum = objectMapper.readValue[Sum](TestData.Data_3)
    val testData3 = new ByteArrayOutputStream()
    objectMapper.writeValue(testData3, data3)

    ctx.collect(testData1.toString)
    ctx.collect(testData2.toString)
    ctx.collect(testData3.toString)


  }

  override def cancel(): Unit = {}
}
class SumDataSink extends SinkFunction[String]{
  override def invoke(value: String, context: SinkFunction.Context): Unit = {
    synchronized{
      SumDataSink.values.add(value)
    }
  }
}
object SumDataSink{
  val values: util.List[String] = new util.ArrayList()
}

class AverageDataSink extends SinkFunction[String]{
  override def invoke(value: String, context: SinkFunction.Context): Unit = {
    synchronized{
      AverageDataSink.values.add(value)
    }
  }
}
object AverageDataSink{
  val values: util.List[String] = new util.ArrayList()
}