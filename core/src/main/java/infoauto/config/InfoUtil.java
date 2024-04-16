package infoauto.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author czz
 * @date 2024/04/10 14:00
 */
@Slf4j
public class InfoUtil {
    //IOT平台IP
    @Value("${platform.ip}")
    private static String ip;
    static final String direction = "MySQL";
    static final String type = "business_log";


    public static void creatdInfo(JSONObject Object, String contentStr) {
        Object = Object.getJSONObject("header");
        log.info(type, direction, getVersion(Object), getTaskId(Object), getTaskType(Object), contentStr, ip);
    }


    public static void creatdInfo(JSONObject Object, String contentStr, String ip) {
        Object = Object.getJSONObject("header");
        logInfo(type, direction, getVersion(Object), getTaskId(Object), getTaskType(Object), contentStr, ip);
    }


    public static void creatdInfo(JSONObject Object, String contentStr, String ip, String taskType) {
        Object = Object.getJSONObject("header");
        logInfo(type, direction, getVersion(Object), getTaskId(Object), taskType, contentStr, ip);
    }


    public static void logInfo(String type, String direction, String version, String taskId, String taskType, String contentStr, String ip) {
        log.info("|{}|{}|{}|{}|{}|{}|{}", type, direction, version, taskId, taskType, contentStr, getNowDate(), ip);
    }


    public static String getNowDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public static String getTaskType(JSONObject Object) {
        return Object.get("taskType").toString();
    }

    public static String getVersion(JSONObject Object) {
        return Object.get("version").toString();
    }

    public static String getTaskId( JSONObject Object) {
        return Object.get("taskId").toString();
    }
}
