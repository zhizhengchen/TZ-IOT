package infoauto.util;

import com.dtflys.forest.Forest;
import com.dtflys.forest.utils.ForestDataType;
import infoauto.entity.ResultData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetPlcForIotUtil {

    /**
     * 从平台获取中机中联工单交互字节内容
     * @param code           设备编号
     */
    public static String getPLCforIOT(String ip ,String code){
        String url= ip+code+"/field-by-code"+"/PLCforIOT";
        ResultData resultData = Forest.get(url).execute(ResultData.class);
        return resultData.getData();

    }


    //写入平台(中机中联）
    public static void sendPostRequest(String ip, String code, int iOTforPLC) {
        //重试次数，需要与配置保持一致
        Integer retryCount=3;
        String requestBody = String.valueOf(iOTforPLC);
        String url= ip+code+"/write-by-code"+"/iOTforPLC";
        Forest.post(url)
                .addBody(requestBody)
                .setContentType("application/json;charset=utf-8")
                .setBodyType(ForestDataType.JSON)
                .onSuccess((data, req, res) -> {
                    // 请求成功时的处理逻辑
                    System.out.println("写入"+iOTforPLC+"成功，返回结果：" + data);
                })
                .onError((data, req, res) -> {
                    // 请求失败时的处理逻辑
                    System.out.println("写入失败，错误信息：" + res.getStatusCode() + " " + res.getStatusCode());
                    String msg=url+"==>重试"+retryCount+"次，连接失败"+data;
                    log.warn(msg);
                })
                .execute();
    }

}
