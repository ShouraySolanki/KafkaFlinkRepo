import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.OutputTag
import org.apache.flink.util.Collector

import java.io.ByteArrayOutputStream



class FlinkProduceFunction extends ProcessFunction[String, String]{

  lazy val state: ValueState[Sum] = getRuntimeContext.getState(new ValueStateDescriptor[Sum]("myState", classOf[Sum]))
  val outputTag = OutputTag[String]("side-output")
  override def processElement(
                               value: String,
                               ctx: ProcessFunction[String, String]#Context,
                               out: Collector[String]): Unit ={
    val objectMapper = new ObjectMapper() with ScalaObjectMapper
    val output = new ByteArrayOutputStream()

    val total: Total = objectMapper.readValue[Total](value)
    val average: Average = objectMapper.readValue[Average](value)
    if (total.getTyp() == "sum") {
      total.setSum(total.getA() + total.getB())
      objectMapper.writeValue(output, total)
    }
    else {
      average.setAverage((average.getA() + average.getB())/2)
      objectMapper.writeValue(output, average)
    }



    out.collect(output.toString)
    ctx.output(outputTag, "sideout-"+ String.valueOf(output))

  }

}

