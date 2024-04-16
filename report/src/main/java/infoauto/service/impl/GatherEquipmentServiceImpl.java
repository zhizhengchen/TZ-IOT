package infoauto.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hvisions.edge.client.setting.EquipmentClient;
import com.hvisions.edge.equipment.EquipmentData;
import com.hvisions.edge.equipment.EquipmentService;
import com.hvisions.edge.equipment.HttpBuilderInstance;
import infoauto.entity.Equipment;
import infoauto.entity.ProductReport;
import infoauto.mapper.GatherEquipmentMapper;
import infoauto.service.GatherEquipmentService;
import infoauto.util.ForwardUtils;
import infoauto.util.KafkaUtils;
import infoauto.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by InfoAuto.
 * 采集设备服务实现
 *
 * @author : zhuangweizhong
 * @create 2023/9/4 10:17
 */
@Slf4j
@Service
public class GatherEquipmentServiceImpl implements GatherEquipmentService {

    @Value("${mom.ip}")
    private String momIp;

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    GatherEquipmentMapper gatherEquipmentMapper;

    @Autowired
    EquipmentService equipmentService;
    @Autowired
    HttpBuilderInstance httpBuilder;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;


    @Override
    public List<Equipment> findAllEquipment() {
        return gatherEquipmentMapper.findAllEquipment();
    }

    @Override
    public List<String> findAllFieldNameFromEquipment(String EquipmentId) {
        return gatherEquipmentMapper.findAllFieldNameFromEquipment(EquipmentId);
    }

    @Override
    public void registerEquipment() {
        //获取所有的设备
        List<Equipment> equipmentIdList = this.findAllEquipment();
        //System.out.println("equipmentIdList:"+equipmentIdList);
        //根据设备id查询该设备下的所有属性
        for (Equipment equipment : equipmentIdList) {
            //获取所有的属性
            List<String> fieldNames = this.findAllFieldNameFromEquipment(equipment.getId());
            //System.out.println("fieldNames:"+fieldNames);
            taskExecutor.execute(() -> {
                equipmentService.subsFieldListByCode(equipment.getCode(), fieldNames, new EquipmentService.FieldConsumer() {
                    @Override
                    public void accept(EquipmentData equipmentInfo, String fieldName, Object value) {
                        log.info("获取到设备{},点位{}的值{}", equipmentInfo.getCode(), fieldName, value);
                        EquipmentClient equipmentClient = httpBuilder.getInstance().getClient(EquipmentClient.class);
                        String jsonString = JSONObject.toJSONString(value);
                        System.out.println("json" + jsonString);
//                    Map<String, Object> map = null;
                        Map<String, Object> map = null;
                        map = JSON.parseObject(jsonString, Map.class);
                        System.out.println("数据报文Map：" + map.toString());

                        Integer plcForIot = Integer.parseInt(map.get("PLCforIOT").toString());
                        System.out.println("PLCforIOT的值" + plcForIot);
                        if (plcForIot == 2) {
                            System.out.println("业务处理:");
                            //判空处理
                            try {
                                extracted(map);
                            } catch (NullPointerException n) {
                                equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC", 4);
                                return;
                            }
                            //数据补充
                            ProductReport productReport = setProductReport(map);
                            ProductReport sendToKafka = setSendToKafkaReport(map);
                            JSONObject jsonObject = getJsonObject(productReport);
                            JSONObject kafkaJson = getJsonObject(sendToKafka);
//                        KafkaUtils.kafkaSendData(kafkaJson,kafkaProducer,topic);
                        JSONObject result = ForwardUtils.postAwaitResult(momIp, "/PRD_IOTReportWork", jsonObject);
//                        JSONObject result = ForwardUtils.postAwaitResult("http://127.0.0.1:8083", "/upload", jsonObject);

//                        System.out.println("result:.............."+result);
                            equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC", 3);

                            //code是接收MOM的处理结果
                            //Integer code = Integer.valueOf(result.getJSONObject("Outputs").getString("code"));
                            byte code = 0;
                            equipmentClient.writeByCode(equipmentInfo.getCode(), "code", code);


                        } else if (plcForIot == 1) {
                            System.out.println("直接返回");
                            equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC", 1);
                        }
                    }


                });

            });

        }

    }

    //判空
    private void extracted(Map<String, Object> map) {
        if (map.get("reqSys") == null || map.get("reqSys").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("facility") == null || map.get("facility").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("wipOrderNo") == null || map.get("wipOrderNo").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("oprSequenceNo") == null || map.get("oprSequenceNo").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("workCenter") == null || map.get("workCenter").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("workStation") == null || map.get("workStation").equals("")) {
            throw new NullPointerException();
        }
        if ((map.get("deviceID") == null || map.get("deviceID").equals("")) && (map.get("employeeNo") == null || map.get("employeeNo").equals(""))) {
            throw new NullPointerException();
        }
        if (map.get("progressType") == null || map.get("progressType").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("unit") == null || map.get("unit").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("completedQuantity") == null || map.get("completedQuantity").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("ifCompleted") == null || map.get("ifCompleted").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("productNo") == null || map.get("productNo").equals("")) {
            throw new NullPointerException();
        }
        if (map.get("serialNo") == null || map.get("serialNo").equals("")) {
            throw new NullPointerException();
        }
    }

    //数据补充
    private ProductReport setProductReport(Map<String, Object> map) {
        ProductReport productReport = new ProductReport();
        productReport.setReqSys(map.get("reqSys") + "");
        productReport.setEmployeeNo(map.get("employeeNo") + "");
        productReport.setProductNo(map.get("productNo") + "");
        productReport.setSerialNo(map.get("serialNo") + "");
        productReport.setFacility(map.get("facility") + "");
        productReport.setWipOrderNo(map.get("wipOrderNo") + "");
        productReport.setWorkStation(map.get("workStation") + "");
        productReport.setWorkCenter(map.get("workCenter") + "");
        productReport.setProgressType(map.get("progressType") + "");
        productReport.setOprSequenceNo(map.get("oprSequenceNo") + "");
        productReport.setIfCompleted(Boolean.parseBoolean(map.get("ifCompleted") + ""));
        productReport.setUnit(map.get("unit") + "");
        productReport.setEquipment(map.get("deviceID") + "");
        productReport.setActualCompleteDate(map.get("actualCompleteDate") + "");
        productReport.setActualStartDate(map.get("actualStartDate") + "");
        productReport.setCompletedQuantity(Integer.parseInt(map.get("completedQuantity") + ""));
        return productReport;
    }

    // 发送kafka数据
    private ProductReport setSendToKafkaReport(Map<String, Object> map) {
        ProductReport productReport = new ProductReport();
        productReport.setReqSys(map.get("reqSys") + "");
        productReport.setEmployeeNo(map.get("employeeNo") + "");
        productReport.setProductNo(map.get("productNo") + "");
        productReport.setSerialNo(map.get("serialNo") + "");
        productReport.setFacility(map.get("facility") + "");
        productReport.setWipOrderNo(map.get("wipOrderNo") + "");
        productReport.setWorkStation(map.get("workStation") + "");
        productReport.setWorkCenter(map.get("workCenter") + "");
        productReport.setProgressType(map.get("progressType") + "");
        productReport.setOprSequenceNo(map.get("oprSequenceNo") + "");
        productReport.setIfCompleted(Boolean.parseBoolean(map.get("ifCompleted") + ""));
        productReport.setUnit(map.get("unit") + "");
        productReport.setEquipment(map.get("deviceID") + "");
        productReport.setActualCompleteDate(map.get("actualCompleteDate") + "");
        productReport.setActualStartDate(map.get("actualStartDate") + "");
        productReport.setCompletedQuantity(Integer.parseInt(map.get("completedQuantity") + ""));
        return productReport;
    }

    //拼接JsonObject(header+body)
    private JSONObject getJsonObject(Object value) {
        //JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("body", value);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("version", "1.0");
        jsonObject3.put("taskId", UUIDUtil.generate());
        jsonObject3.put("taskType", "MFGR0004");

        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("header", jsonObject3);
        jsonObject4.putAll(jsonObject2);
        return jsonObject4;
    }
}
