package infoauto.service.zp.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import infoauto.config.InfoUtil;
import infoauto.entity.Equipment;
import infoauto.entity.ProductOrder;
import infoauto.mapper.LogsMapper;
import infoauto.mapper.ZjzlEquipmentMapper;
import infoauto.service.zp.MomDownService;
import infoauto.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author czz
 * @date 2024-04-10 10:00
 * @content 增加写入成功返回值, 根据返回值去修改下发是否成功;
 */
//处理下发中机中联业务
@Slf4j
@Service
public class MomDownServiceImpl implements MomDownService {

    //IOT平台IP
    @Value("${platform.ip}")
    private String ip;

    @Value("${zjzl.scjcode}")
    private String scjcode;

    @Value("${zjzl.xcjcode}")
    private String xcjcode;

    @Value("${zjzl.scjproductNo}")
    private String scjproductNo;

    @Value("${zjzl.xcjproductNo}")
    private String xcjproductNo;

    @Resource
    private LogsMapper logsMapper;

    @Autowired
    ZjzlEquipmentMapper zjzlEquipmentMapper;

    /**
     * 下发工单给中机中联设备
     */
    @Override
    public void momDownZJZL(String lineCode) throws InterruptedException {
        //队列中取出工单jsonObject
        JSONObject jsonObject = IotQueueUtil.getFromQueue();
        String code = "";
        String equipmentCode = UrlUtils.LINECODEMAP.get(lineCode);
//        this.insertData(jsonObject); //数据存表
        for (Equipment equipment : zjzlEquipmentMapper.findEquipment1()) {
            code = equipment.getCode();
            //判断code数据哪个产线的
            if (code.equals(equipmentCode)) {
                WriteInUtil.writeIn1.setFieldName("wipOrderNo");
                WriteInUtil.writeIn1.setValue(jsonObject.getJSONObject("body").getString("wipOrderNo"));

                WriteInUtil.writeIn2.setFieldName("progressStatus");
                WriteInUtil.writeIn2.setValue(jsonObject.getJSONObject("body").getString("progressStatus"));

                WriteInUtil.writeIn3.setFieldName("serialNo");
                WriteInUtil.writeIn3.setValue(jsonObject.getJSONObject("body").getString("serialNo"));

                WriteInUtil.writeIn4.setFieldName("productType");
                WriteInUtil.writeIn4.setValue(jsonObject.getJSONObject("body").getString("productType"));

                WriteInUtil.writeIn5.setFieldName("scheduledStartDate");
                WriteInUtil.writeIn5.setValue(jsonObject.getJSONObject("body").getString("scheduledStartDate"));

                WriteInUtil.writeIn6.setFieldName("scheduledCompleteDate");
                WriteInUtil.writeIn6.setValue(jsonObject.getJSONObject("body").getString("scheduledCompleteDate"));

                WriteInUtil.writeIn7.setFieldName("productNo");
                WriteInUtil.writeIn7.setValue(jsonObject.getJSONObject("body").getString("productNo"));

                WriteInUtil.writeIn8.setFieldName("productionLineNo");
                WriteInUtil.writeIn8.setValue(lineCode);

                WriteInUtil.writeIn9.setFieldName("facility");
                WriteInUtil.writeIn9.setValue(jsonObject.getJSONObject("body").getString("facility"));

                WriteInUtil.writeIn10.setFieldName("iOTforPLC");
                WriteInUtil.writeIn10.setValue("2");

                JSONArray array = new JSONArray();
                array.add(JSONObject.toJSON(WriteInUtil.writeIn1));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn2));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn3));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn4));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn5));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn6));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn7));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn8));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn9));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn10));
                System.out.println(array);
                //写入
                ForwardUtils.postAsyncZjzl(ip, code, array, jsonObject);
                Thread.sleep(1000);
                //查询交互字节
                 workOrderDown(code, array,jsonObject);

            }
        }

    }

    //中机中联工件绑定工单下发
    @Override
    public void momDownBinding(String productionLineNo) throws InterruptedException {

        //队列中取出工单jsonObject
        JSONObject jsonObject = IotQueueUtil.getFromQueue2();
        String equipmentCode = UrlUtils.LINECODEMAP.get(productionLineNo);
        String code = "";
        //this.insertData(jsonObject); //数据存表
        for (Equipment equipment : zjzlEquipmentMapper.findEquipment()) {
            code = equipment.getCode();
            if (code.equals(equipmentCode)) {
                WriteInUtil.writeIn1.setFieldName("productionLineNo");
                WriteInUtil.writeIn1.setValue(productionLineNo);

                WriteInUtil.writeIn2.setFieldName("wipOrderNo");
                WriteInUtil.writeIn2.setValue(jsonObject.getJSONObject("body").getString("wipOrderNo"));

                WriteInUtil.writeIn3.setFieldName("serialNo");
                WriteInUtil.writeIn3.setValue(jsonObject.getJSONObject("body").getString("serialNo"));

                WriteInUtil.writeIn4.setFieldName("workCenter");
                WriteInUtil.writeIn4.setValue(jsonObject.getJSONObject("body").getString("workCenter"));

                WriteInUtil.writeIn5.setFieldName("oprSequenceNo");
                WriteInUtil.writeIn5.setValue(jsonObject.getJSONObject("body").getString("oprSequenceNo"));

                WriteInUtil.writeIn6.setFieldName("workStation");
                WriteInUtil.writeIn6.setValue(jsonObject.getJSONObject("body").getString("workStation"));

                WriteInUtil.writeIn7.setFieldName("issueTime");
                WriteInUtil.writeIn7.setValue(jsonObject.getJSONObject("body").getString("issueTime"));

                WriteInUtil.writeIn8.setFieldName("iOTforPLC");
                WriteInUtil.writeIn8.setValue("2");

                JSONArray array = new JSONArray();
                array.add(JSONObject.toJSON(WriteInUtil.writeIn1));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn2));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn3));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn4));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn5));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn6));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn7));
                array.add(JSONObject.toJSON(WriteInUtil.writeIn8));
                System.out.println(array);
                //写入
                ForwardUtils.postAsyncZjzl(ip, code, array);

                Thread.sleep(1000);
                //查询交互字节
                workOrderDown(code, array);
            }
        }

    }

    /**
     * @param code  设备号
     * @param array 内容
     * @return true 写入成功,false 写入失败
     * @throws InterruptedException
     * @author czz
     */
//    public Boolean workOrderDown(String code, JSONArray array) throws InterruptedException {
//        //获取PLC工单的交互字节内容
//        Thread.sleep(5000);
//        String plCforIOT = GetPlcForIotUtil.getPLCforIOT(ip, code);
//        if ("3".equals(plCforIOT)) {
//            //iotForPlc写1
//            GetPlcForIotUtil.sendPostRequest(ip, code, 1);
//            //休眠1s
//            Thread.sleep(2000);
//            //再获取一次PLCforIOT内容
//            String plCforIOT2 = GetPlcForIotUtil.getPLCforIOT(ip, code);
//            if ("1".equals(plCforIOT2)) {
//                System.out.println("写入交互完成，返回结果：" + plCforIOT2);
//                return true;
//            } else {
//                //再次写1
//                GetPlcForIotUtil.sendPostRequest(ip, code, 1);
//            }
//        } else {
//            //再调一次写2
//            ForwardUtils.postAsyncZjzl(ip, code, array);
//        }
//        return false;
//    }

    /**
     * @param code  设备号
     * @param array 内容
     * @return true 写入成功,false 写入失败
     * @throws InterruptedException
     * @author czz
     */
    public void workOrderDown(String code, JSONArray array) throws InterruptedException {
        boolean result = true;
        int count = 0;
        while (result) {
            Thread.sleep(2000);
            //获取PLC工单的交互字节内容
            String plCforIOT = GetPlcForIotUtil.getPLCforIOT(ip, code);
            if ("3".equals(plCforIOT)) {
                //iotForPlc写1
                if (count == 0){
                    GetPlcForIotUtil.sendPostRequest(ip, code, 1);
                    count++;
                }
                //休眠1s
                Thread.sleep(1000);
                //再获取一次PLCforIOT内容
                String plCforIOT2 = GetPlcForIotUtil.getPLCforIOT(ip, code);
                if ("1".equals(plCforIOT2)) {
                    System.out.println("写入交互完成，返回结果：" + plCforIOT2);
                }
            }
        }
    }
    /**
     * 重写方法加入JsonObject参数
     * @param code  设备号
     * @param array 内容
     * @param jsonObject Json报文
     * @return true 写入成功,false 写入失败
     * @throws InterruptedException
     * @author czz
     */
    public void workOrderDown(String code, JSONArray array,JSONObject jsonObject) throws InterruptedException {
        boolean result = true;
        int count = 0;
        while (result) {
            Thread.sleep(2000);
            //获取PLC工单的交互字节内容
            String plCforIOT = GetPlcForIotUtil.getPLCforIOT(ip, code);
            if ("3".equals(plCforIOT)) {
                //iotForPlc写1
                if (count == 0){
                    GetPlcForIotUtil.sendPostRequest(ip, code, 1);
                    count++;
                }
                //休眠1s
                Thread.sleep(2000);
                //再获取一次PLCforIOT内容
                String plCforIOT2 = GetPlcForIotUtil.getPLCforIOT(ip, code);
                if ("1".equals(plCforIOT2)) {
                    System.out.println("写入交互完成，返回结果：" + plCforIOT2);
                    InfoUtil.creatdInfo(jsonObject, "写入成功,设备:" + code + ip + ",内容:" + array, ip, "工单下发中机中联写入设备");
                    result = false;
                }
            }
        }
    }

    @Async
    void insertData(JSONObject value) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setTaskId(value.getJSONObject("body").getString("taskId"));
        productOrder.setWipOrderNo(value.getJSONObject("body").getString("wipOrderNo"));
        productOrder.setWipOrderType(value.getJSONObject("body").getString("wipOrderType"));
        zjzlEquipmentMapper.addData(productOrder);
    }


}
