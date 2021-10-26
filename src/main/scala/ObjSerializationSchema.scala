/*
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper

class ObjSerializationSchema(var topic: String) extends KafkaSerializationSchema[Total] {
  private var mapper: ObjectMapper = new ObjectMapper()

  override def serialize(obj: Total): ProducerRecord[Array[Byte], Array[Byte]] = {
    var message = null
    if (mapper == null) mapper = new ObjectMapper()
    try message = mapper.writeValueAsBytes(obj)
    catch {
      case e: JsonProcessingException => throw e

      // TODO
    }
    new ProducerRecord[Array[Byte], Array[Byte]](topic, message)
  }
}*/
