package infoauto.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description
 * @Author Administrator
 * @Date 2024/3/21 14:49
 * @Version 1.0
 */
public class KafkaUtils {



    public static void kafkaSendData(JSONObject jsonObject,KafkaProducer kafkaProducer,String topic){
        String string = jsonObject.toJSONString();
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, string);
        kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
            if (e != null) {
                // 处理失败的情况
                System.out.println(e);
            } else {
                // 处理成功的情况
                System.out.println("发送到kafka的数据:"+jsonObject);
                System.out.println(recordMetadata);
            }
        });
    }
}
