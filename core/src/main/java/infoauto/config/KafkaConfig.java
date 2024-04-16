package infoauto.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @Author Administrator
 * @Date 2024/3/20 9:13
 * @Version 1.0
 */
@Configuration
public class KafkaConfig {
    @Bean
    public KafkaProducer kafkaProducer() {
        Map<String, Object> configs = new HashMap<>();
        //#kafka服务端的IP和端口,格式:(ip:port)
//        configs.put("bootstrap.servers", "10.79.129.125:32092");
        configs.put("bootstrap.servers", "10.79.129.125:9092");
        //客户端发送服务端失败的重试次数
        configs.put("retries", 2);
        //多个记录被发送到同一个分区时,生产者将尝试将记录一起批处理成更少的请求.
        //此设置有助于提高客户端和服务器的性能,配置控制默认批量大小(以字节为单位)
        configs.put("batch.size", 16384);
        //生产者可用于缓冲等待发送到服务器的记录的总内存字节数(以字节为单位)
        configs.put("buffer-memory", 33554432);
        configs.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000);
        //生产者producer要求leader节点在考虑完成请求之前收到的确认数,用于控制发送记录在服务端的持久化
        //acks=0,设置为0,则生产者producer将不会等待来自服务器的任何确认.该记录将立即添加到套接字(socket)缓冲区并视为已发送.在这种情况下,无法保证服务器已收到记录,并且重试配置(retries)将不会生效(因为客户端通常不会知道任何故障),每条记录返回的偏移量始终设置为-1.
        //acks=1,设置为1,leader节点会把记录写入本地日志,不需要等待所有follower节点完全确认就会立即应答producer.在这种情况下,在follower节点复制前,leader节点确认记录后立即失败的话,记录将会丢失.
        //acks=all,acks=-1,leader节点将等待所有同步复制副本完成再确认记录,这保证了只要至少有一个同步复制副本存活,记录就不会丢失.
        configs.put("acks", "0");
        //指定key使用的序列化类
        Serializer keySerializer = new StringSerializer();
        //指定value使用的序列化类
        Serializer valueSerializer = new StringSerializer();
        //创建Kafka生产者
        KafkaProducer kafkaProducer = new KafkaProducer(configs, keySerializer, valueSerializer);
        return kafkaProducer;
    }


}