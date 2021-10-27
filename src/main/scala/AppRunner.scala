import com.typesafe.config.ConfigFactory
import configurations.FlinkSampleConfiguration
import flinkjobs.FlinkKafkaProcessor

object AppRunner {

  def main(args: Array[String]): Unit ={
    val config = ConfigFactory.load("application.conf").getConfig("com.ram.batch")
    //val jsonConfig = config.getConfig("json")
    /*val jsonconfig = jsonConfig.getString("test")
    print(jsonconfig)*/

    val flinkSampleConfiguration = new FlinkSampleConfiguration(config)
    val  flinkConsume = new FlinkKafkaProcessor(flinkSampleConfiguration)

    flinkConsume.process()
  }

}
