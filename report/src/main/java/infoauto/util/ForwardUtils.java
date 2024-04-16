package infoauto.util;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.Forest;
import com.dtflys.forest.utils.ForestDataType;
import infoauto.anno.MyLog;
import infoauto.enums.LogType;
import infoauto.enums.SaveDirection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * Created by InfoAuto.
 *  分发工具类
 * @author : zhuangweizhong
 * @create 2023/5/22 16:18
 */
@Slf4j
@Component
@Data
public class ForwardUtils {

    /**
     * 请求结果等待反馈，用于生产报工接口
     * @param ip                请求ip
     * @param controller        请求controller
     * @param o                 内容
     * @return
     */
    public static JSONObject postAwaitResult(String ip,String controller,JSONObject o){

        //存业务日志
        String logType = LogType.BUSINESS.getName();
        String direction = SaveDirection.MYSQL.getName();//存储位置
        String taskId = o.getJSONObject("header").getString("taskId");
        String version = o.getJSONObject("header").getString("version");
        String taskType = o.getJSONObject("header").getString("taskType");
        String stringJson = o.toJSONString();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,version,taskId,taskType,stringJson,time);

        //报文包装
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject.put("Data", o);
        jsonObject.put("Data",jsonObject.toJSONString());
        jsonObject2.put("Inputs",jsonObject);
        System.out.println(jsonObject2);

        Integer retryCount=3;
        String url=ip+"/"+controller;
        ArrayList<JSONObject> arrayList =new ArrayList<>();
        Forest.post(url).addBody(jsonObject2).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onSuccess((data,req,res)->{
            arrayList.add((JSONObject) data);
        }).onError((ex,req,res)->{
            String msg=url+"==>重试"+retryCount+"次，连接失败";
            //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
            log.warn(msg);
        }).execute();
        //返回结果
        if (arrayList.size()>0){
            return arrayList.get(0);
        }else {
            return null;
        }
    }

}
