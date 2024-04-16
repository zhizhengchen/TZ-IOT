package infoauto.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import infoauto.anno.MyLog;
import infoauto.util.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @Author Administrator
 * @Date 2024/3/20 9:16
 * @Version 1.0
 */
@Slf4j
@RestController("test1")
@RequestMapping("test1")
public class TestController {

    @Value("${kafka.topic}")
    private  String topic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/sendKafka")
    public String sendKafka( @RequestBody JSONObject jsonObject){
        try {
            //1.获取业务数据
            Map<String,String> map = new HashMap<>();
            String cityStr = JSONObject.toJSONString(map);
            //2.使用KafkaProducer向Kafka写入数据
            String string = jsonObject.toJSONString();
            KafkaUtils.kafkaSendData(jsonObject,kafkaProducer,topic);
            System.out.println("执行完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "写入成功";

    }

    @PostMapping("/toTxt")
    public String toTxt(@RequestBody JSONObject jsonObject){
        try {
            // 创建一个 ObjectMapper 对象
            ObjectMapper mapper = new ObjectMapper();
            // 将 JSON 对象转换为字符串
            String jsonString = mapper.writeValueAsString(jsonObject);
            // 将字符串写入到文件
            String currentDir = System.getProperty("user.dir");
            System.out.println("当前工作目录：" + currentDir);
            String folderPath = "/log"; // 设置文件夹路径
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String path = currentDir+folderPath+"/"+date;
            File folder = new File(path);

            // 判断文件夹是否存在
            if (!folder.exists()) {
                // 创建文件夹
                boolean created = folder.mkdirs();
                if (created) {
                    System.out.println("文件夹已成功创建！");
                } else {
                    System.out.println("文件夹创建失败！");
                }
            } else {
                System.out.println("文件夹已存在！");
            }
            FileWriter writer = new FileWriter(path+"/data.txt");
            writer.write(jsonString);
            writer.close();
            System.out.println("JSON写入成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return "写入失败";
        }
        return "写入成功";
    }

    @PostMapping("/write")
    public String write( @MyLog @RequestBody JSONObject jsonObject){
//        log.info("|{}|{}|{}|{}|{}|{}|{}", type ,direction,version,taskId,taskType,stringJson,time,ip);
        return "success";
    }
}
