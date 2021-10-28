package testspec


import com.google.gson.Gson
import com.typesafe.config.{Config, ConfigFactory}
import configurations.FlinkSampleConfiguration
import jsondata.TestData
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.test.util.{MiniClusterResourceConfiguration, MiniClusterWithClientResource}
import org.mockito.Mockito
import org.mockito.Mockito.when
import org.mockito.MockitoSugar.mock
import util.{FlinkKafkaConnector, Sum}

import java.util

class FlinkKafkaProcessorTest() {

  val flinkCluster = new MiniClusterWithClientResource(new MiniClusterResourceConfiguration.Builder()
    .setNumberSlotsPerTaskManager(1)
    .setNumberTaskManagers(1)
    .build)

  val config: Config = ConfigFactory.load("test.conf")
  val flinkSampleConfiguration = new FlinkSampleConfiguration(config)

  val mockKafkaConnector: FlinkKafkaConnector = mock[FlinkKafkaConnector](Mockito.withSettings().serializable())


    when(mockKafkaConnector.kafkaConsumer(flinkSampleConfiguration.jsontest))thenReturn(new FlinkSampleEventSource)

    when(mockKafkaConnector.kafkaProducer(flinkSampleConfiguration.jsontest1))thenReturn(new SumDataSink)

    when(mockKafkaConnector.kafkaProducer(flinkSampleConfiguration.jsontest2))thenReturn()




}

class FlinkSampleEventSource extends SourceFunction[String]{
  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {

    val gson = new Gson()
    val testData1 = gson.fromJson(TestData.Data_1, Sum.getClass)
    val testData2 = gson.fromJson(TestData.Data_2, Sum.getClass)
    val testData3 = gson.fromJson(TestData.Data_3, Sum.getClass)

    ctx.collect(testData1.toString)
    ctx.collect(testData2.toString)
    ctx.collect(testData3.toString)


  }

  override def cancel(): Unit = {}

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
}