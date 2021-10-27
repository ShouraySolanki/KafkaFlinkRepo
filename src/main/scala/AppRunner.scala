import com.typesafe.config.ConfigFactory
import configurations.FlinkSampleConfiguration
import flinkjobs.FlinkKafkaProcessor

object AppRunner {

  def main(args: Array[String]): Unit ={
    val config = ConfigFactory.load("src/main/resources/flinkkafkaprocessor.conf").getConfig("basic-config")
    val flinkSampleConfiguration = new FlinkSampleConfiguration(config)
    val  flinkConsume = new FlinkKafkaProcessor(flinkSampleConfiguration)
    flinkConsume.process()
  }

}
