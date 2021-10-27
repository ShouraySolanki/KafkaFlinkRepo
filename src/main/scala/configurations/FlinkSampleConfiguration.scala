package configurations

import com.typesafe.config.Config

class FlinkSampleConfiguration(val config: Config ) extends Serializable {

// Kafka topics

  val jsontest:String = config.getString("json.test")
  val jsontest1:String = config.getString("json.test1")
  val jsontest2:String = config.getString("json.test2")

  //val sumOutputTag = OutputTag[String]("sum-output")

}
