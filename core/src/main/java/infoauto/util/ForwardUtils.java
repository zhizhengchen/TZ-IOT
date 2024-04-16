package infoauto.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.Forest;
import com.dtflys.forest.utils.ForestDataType;
import infoauto.config.InfoUtil;
import infoauto.controller.mom.MOMController;
import infoauto.dao.UrlMapper;
import infoauto.enums.LogType;
import infoauto.enums.SaveDirection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by InfoAuto.
 * 分发工具类
 *
 * @author : zhuangweizhong
 * @create 2023/5/22 16:18
 */
@Slf4j
@Component
@Data
public class ForwardUtils {
    /**
     * 向中控集合分发Ip(基础数据)
     *
     * @param listip     中控ip集合
     * @param o          请求内容
     * @param controller 请求地址的指定controller ，Ip后缀
     */

    public static void postAsyncToZKBaseTecos(List<String> listip, JSONObject o, String controller) {

        //重试次数，需要与配置保持一致
        Integer retryCount = 3;
        listip.stream().forEach(ip -> {
            String url = ip + "/" + controller;
            // TODO 下料华工需要加TOKEN
//            String url=ip+"/"+controller+ MOMController.TOKEN;
            System.out.println(url);
            Forest.post(url).addBody(o).setTimeout(120000).setMaxRetryInterval(120000L).setBodyType(ForestDataType.JSON).setContentType("application/json;charset=utf-8").onError((ex, req, res) -> {
                //ex为运行异常，req为请求，res为响应

                String msg = url + "重试" + retryCount + "次，连接失败";
                //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
                log.warn(msg);
            }).onSuccess((ex, req, res) -> {
                //获取返回结果
                System.out.println(url + "返回结果：" + res.getResult());
            }).async().execute();
        });
    }

    /**
     * 向中控集合分发Ip(携带参数)
     *
     * @param listip     中控ip集合
     * @param o          请求内容
     * @param controller 请求地址的指定controller ，Ip后缀
     */

    public static void postAsyncToZKTecos(List<String> listip, JSONObject o, String controller) {
        //重试次数，需要与配置保持一致
        Integer retryCount = 3;
        listip.stream().forEach(ip -> {
            String url = ip + "/" + controller;
            // TODO 华工下料车间ip需要加TOKEN
//            String url=ip+"/"+controller+MOMController.TOKEN;
            System.out.println(url);
            Forest.post(url).addBody(o).setBodyType(ForestDataType.JSON).setContentType("application/json;charset=utf-8").onError((ex, req, res) -> {
                //ex为运行异常，req为请求，res为响应

                String msg = url + "重试" + retryCount + "次，连接失败";
                //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
                log.warn(msg);
            }).onSuccess((ex, req, res) -> {
                //获取返回结果
                System.out.println(url + "返回结果：" + res.getResult());
                InfoUtil.creatdInfo(o, "下发成功", ip, "工单下发");
            }).async().execute();

        });
    }

    /**
     * 向中控集合分发Ip(携带参数)
     *
     * @param listip     中控ip集合
     * @param o          请求内容
     * @param controller 请求地址的指定controller ，Ip后缀
     */

    public static void postAsyncToZKTecos(List<String> listip, JSONObject o, String controller, String productionLineNameStr) {
        //重试次数，需要与配置保持一致
        Integer retryCount = 3;
        listip.stream().forEach(ip -> {
            String url = ip + "/" + controller;
            // TODO 华工下料车间ip需要加TOKEN
//            String url=ip+"/"+controller+MOMController.TOKEN;
            System.out.println(url);
            Forest.post(url).addBody(o).setBodyType(ForestDataType.JSON).setContentType("application/json;charset=utf-8").onError((ex, req, res) -> {
                //ex为运行异常，req为请求，res为响应

                String msg = url + "重试" + retryCount + "次，连接失败";
                InfoUtil.creatdInfo(o, "下发失败->" + productionLineNameStr + msg, ip, "工单下发");
                //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
                log.warn(msg);
            }).onSuccess((ex, req, res) -> {
                //获取返回结果
                System.out.println(url + "返回结果：" + res.getResult());
                InfoUtil.creatdInfo(o, "下发成功->" + productionLineNameStr, ip, "工单下发");
            }).async().execute();

        });
    }

    /**
     * 向MOM请求(携带参数)
     *
     * @param ip         ip地址
     * @param controller 请求地址的指定controller
     * @param o          请求内容
     */
    public static void postAsyncMOM(String ip, String controller, JSONObject o) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject.put("Data", o);
        jsonObject.put("Data", jsonObject.toJSONString());
        jsonObject2.put("Inputs", jsonObject);

        Integer retryCount = 3;
        //String url = "http://127.0.0.1:8083" + "/" + "upload";
        String url = ip + "/" + controller;
        Forest.post(url).addBody(jsonObject2).setMaxRetryInterval(120000L).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onError((ex, req, res) -> {

            String msg = url + "重试" + retryCount + "次，连接失败";
            //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
            log.warn(msg);
        }).onSuccess((ex, req, res) -> {
            //
            //获取返回结果
            System.out.println(url + "返回结果：" + res.getResult());
        }).async().execute();
    }

    /**
     * 向指定的中控(携带参数)
     *
     * @param ip         ip地址
     * @param controller 请求地址的指定controller
     * @param o          请求内容
     */
    public static void postAsyncZK(String ip, String controller, JSONObject o) {

        Integer retryCount = 3;
        String url = ip + "/" + controller;
//        String url=ip+"/"+controller+MOMController.TOKEN;
        Forest.post(url).addBody(o).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onError((ex, req, res) -> {

            String msg = url + "重试" + retryCount + "次，连接失败";
            //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
            log.warn(msg);
        }).onSuccess((ex, req, res) -> {
            //获取返回结果
            System.out.println(url + "返回结果：" + res.getResult());
        }).async().execute();
    }

    /**
     * 向指定的中控(携带参数)、下料中控需要加token
     *
     * @param ip         ip地址
     * @param controller 请求地址的指定controller
     * @param o          请求内容
     */
    public static void postAsyncXLZK(String ip, String controller, JSONObject o) {

        Integer retryCount = 3;
        String url = ip + "/" + controller;
        Forest.post(url).addBody(o).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onError((ex, req, res) -> {

            String msg = url + "重试" + retryCount + "次，连接失败";
            //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
            log.warn(msg);
        }).onSuccess((ex, req, res) -> {
            //获取返回结果
            System.out.println(url + "返回结果：" + res.getResult());
        }).async().execute();
    }

    /**
     * 请求结果等待反馈，用于生产报工接口
     *
     * @param ip         请求ip
     * @param controller 请求controller
     * @param o          内容
     * @return
     */
    public static JSONObject postAwaitResult(String ip, String controller, JSONObject o) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject.put("Data", o);
        jsonObject.put("Data", jsonObject.toJSONString());
        jsonObject2.put("Inputs", jsonObject);
        System.out.println(jsonObject2);

        Integer retryCount = 3;
//        String url = "http://127.0.0.1:8083" + "/" + "upload";
        String url = ip + "/" + controller;
        ArrayList<JSONObject> arryList = new ArrayList<>();
        Forest.post(url).addBody(jsonObject2).setMaxRetryInterval(120000L).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onSuccess((data, req, res) -> {
            arryList.add((JSONObject) data);
        }).onError((ex, req, res) -> {

            String msg = url + "重试" + retryCount + "次，连接失败";
            //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
            log.warn(msg);
        }).onSuccess((ex, req, res) -> {
            //获取返回结果
            System.out.println(url + "返回结果：" + res.getResult());
        }).async().execute();
        //返回结果
        if (arryList.size() > 0) {
            return arryList.get(0);
        } else {
            return null;
        }

    }

    /**
     * @param ip         请求ip
     * @param controller 请求controller
     * @param o          内容
     * @param name       名称
     * @param typeName   类型
     * @return
     */
    public static JSONObject postAwaitResult(String ip, String controller, JSONObject o, String name, String typeName) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject.put("Data", o);
        jsonObject.put("Data", jsonObject.toJSONString());
        jsonObject2.put("Inputs", jsonObject);
        System.out.println(jsonObject2);

        Integer retryCount = 3;
        String url = ip + "/" + controller;
        ArrayList<JSONObject> arryList = new ArrayList<>();
        Forest.post(url).addBody(jsonObject2).setMaxRetryInterval(120000L).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onSuccess((data, req, res) -> {
            arryList.add((JSONObject) data);
        }).onError((ex, req, res) -> {
            String msg = url + "重试" + retryCount + "次，连接失败";
            InfoUtil.creatdInfo(o, name + typeName + "失败 : " + o, ip, typeName);
            log.warn(msg);
        }).onSuccess((ex, req, res) -> {
            //获取返回结果
            System.out.println(url + "返回结果：" + res.getResult());
            InfoUtil.creatdInfo(o, name + typeName + "成功 : " + o, ip, typeName);
        }).async().execute();
        //返回结果
        if (arryList.size() > 0) {
            return arryList.get(0);
        } else {
            return null;
        }

    }

    /**
     * @param ip
     * @param controller
     * @param o
     * @return
     */
    public static JSONObject postAwaitResultAndUpload(String ip, String controller, JSONObject o) {
//        JSONObject jsonObject = new JSONObject();
//        JSONObject jsonObject2 = new JSONObject();
//        jsonObject.put("Data", o);
//        jsonObject.put("Data", jsonObject.toJSONString());
//        jsonObject2.put("Inputs", jsonObject);
//        System.out.println(jsonObject2);

        Integer retryCount = 3;
        //String url = "http://127.0.0.1:8083" + "/" + "upload";
        // 下料中控需要加token
        String url = ip + "/" + controller;
        ArrayList<JSONObject> arryList = new ArrayList<>();
        Forest.post(url).addBody(o).setMaxRetryInterval(120000L).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onSuccess((data, req, res) -> {
            arryList.add((JSONObject) data);
        }).onError((ex, req, res) -> {

            String msg = url + "重试" + retryCount + "次，连接失败";
            //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,ip,controller,retryCount,o.toJSONString(),msg);
            log.warn(msg);
        }).onSuccess((ex, req, res) -> {
            //获取返回结果
            System.out.println(url + "返回结果：" + res.getResult());
        }).async().execute();
        //返回结果
        if (arryList.size() > 0) {
            return arryList.get(0);
        } else {
            return null;
        }

    }
//    /**
//     * 向中机中联写入(写入平台）
//     * @param code           设备编号
//     * @param o             请求内容
//     */
//    public static void postAsyncZjzl(String code, JSONArray o){
//
//        Integer retryCount=3;
//        String url="http://10.79.32.20:9700/equipment/"+code+"/write-by-code/values";
//        Forest.post(url).addBody(o).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onError((ex,req,res)->{
//
//            String msg=url+"重试"+retryCount+"次，连接失败";
//            //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,url,retryCount,o.toJSONString(),msg);
//            log.warn(msg);
//        }).async().execute();
//    }

    /**
     * 向中机中联写入(写入平台）
     *
     * @param code 设备编号
     * @param o    请求内容
     */
    public static void postAsyncZjzl(String ip, String code, JSONArray o) {
        Integer retryCount = 3;
        String url = ip + code + "/write-by-code/values";
        Forest.post(url).addBody(o).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onSuccess((data, req, res) -> {
            System.out.println("写入成功" + data.toString());
        }).onError((ex, req, res) -> {
            /**
             * 将日志存储到Elasticsearch
             * 日志类型
             */
            String logType = LogType.REQUEST_TIME_OUT.getName();
            //存储位置
            String direction = SaveDirection.ELASTICSEARCH.getName();
            String msg = url + "==>重试" + retryCount + "次，连接失败" + ex;
            //log.info("|{}|{}|{}|{}|{}|{}|{}",logType,direction,url,retryCount,o.toJSONString(),msg);
            log.warn(msg);
        }).execute();
    }

    /**
     * 重写 postAsyncZjzl方法 加入原格式
     * 向中机中联写入(写入平台）
     *
     * @param ip
     * @param code       设备编号
     * @param o          请求内容
     * @param jsonObject
     */
    public static void postAsyncZjzl(String ip, String code, JSONArray o, JSONObject jsonObject) {
        Integer retryCount = 3;
        String url = ip + code + "/write-by-code/values";
        Forest.post(url).addBody(o).setContentType("application/json;charset=utf-8").setBodyType(ForestDataType.JSON).onSuccess((data, req, res) -> {
            System.out.println("写入平台成功" + data.toString());
            InfoUtil.creatdInfo(jsonObject, "写入成功,设备:" + code + "内容:" + o, ip, "工单下发中机中联写入平台");
        }).onError((ex, req, res) -> {
            /**
             * 将日志存储到Elasticsearch
             * 日志类型
             */
//            String logType = LogType.REQUEST_TIME_OUT.getName();
//            //存储位置
//            String direction = SaveDirection.ELASTICSEARCH.getName();
            String msg = url + "==>重试" + retryCount + "次，连接失败" + ex;
            InfoUtil.creatdInfo(jsonObject, "写入失败,设备:" + code + "内容:" + o, ip, "工单下发中机中联写入平台");
            log.warn(msg);
        }).execute();
    }

}
