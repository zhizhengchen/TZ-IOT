package infoauto.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hvisions.edge.client.setting.EquipmentClient;
import com.hvisions.edge.equipment.EquipmentData;
import com.hvisions.edge.equipment.EquipmentService;
import com.hvisions.edge.equipment.HttpBuilderInstance;
import infoauto.entity.AndonAlarm;
import infoauto.entity.Equipment;
import infoauto.entity.EquipmentAlarms;
import infoauto.entity.EquipmentStatus;
import infoauto.mapper.GatherEquipmentMapper;
import infoauto.service.GatherEquipmentService;
import infoauto.util.ForwardUtils;
import infoauto.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by InfoAuto.
 * 采集设备服务实现
 * @author : zhuangweizhong
 * @create 2023/9/4 10:17
 */
@Slf4j
@Service
public class GatherEquipmentServiceImpl implements GatherEquipmentService {

    @Value("${mom.ip}")
    private String momIp;
    @Autowired
    GatherEquipmentMapper gatherEquipmentMapper;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    HttpBuilderInstance httpBuilder;


    //设备状态、报警采集上传
    @Override
    public void registerEquipment() {
        //获取所有的设备
        List<Equipment> equipmentIdList = gatherEquipmentMapper.findAllEquipment();
        //System.out.println("equipmentIdList:"+equipmentIdList);
        //业务处理
        processUpload(equipmentIdList);

    }

    //安灯采集上传
    @Override
    public void andonEquipment() {
        List<Equipment> allAndonEquipment = gatherEquipmentMapper.fillAllAndonEquipment();
        //System.out.println("allAndonEquipmentList:"+allAndonEquipment);
        //业务处理
        andonProcess(allAndonEquipment);
    }


    private void processUpload(List<Equipment> equipmentIdList) {
        for (Equipment equipment : equipmentIdList) {
            //根据设备id查询该设备下的所有属性
            List<String> fieldNames= gatherEquipmentMapper.findAllFieldNameFromEquipment(equipment.getId());
            //System.out.println("fieldNames:"+fieldNames);
            equipmentService.subsFieldListByCode(equipment.getCode(), fieldNames, new EquipmentService.FieldConsumer() {
                @Override
                public void accept(EquipmentData equipmentInfo, String fieldName, Object data) {
                    log.info("获取到设备{},点位{}的值{}", equipmentInfo.getCode(), fieldName, data);
                    String timeStr= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
                    //object转map
                    Map<String,Object> dataMap = (Map) data;

                    //状态
                    if (dataMap.get("status") != null && dataMap.get("status") != ""){
                        System.out.println("status:"+dataMap.get("status"));
                        EquipmentStatus equipmentStatus = new EquipmentStatus();
                        equipmentStatus.setDeviceID(dataMap.get("deviceID")+"");
                        equipmentStatus.setValue(dataMap.get("status")+"");
                        equipmentStatus.setUpdateTime(timeStr);
                        //System.out.println("equipmentStatus::"+equipmentStatus);

                        JSONObject jsonObject = getJsonObject(equipmentStatus);
                        ForwardUtils.postAsyncMOM(momIp, "/EAM_IOTEQUStatus_Receive", jsonObject);
//                        ForwardUtils.postAsyncMOM("http://127.0.0.1:8083", "/upload", jsonObject);
                    };
                    //报警
                    if (dataMap.get("alarm") != null && dataMap.get("alarm") != ""){
                        System.out.println("alarm:"+dataMap.get("alarm"));
                        EquipmentAlarms equipmentAlarms = new EquipmentAlarms();
                        EquipmentAlarms.Exception exception = new EquipmentAlarms.Exception();
                        equipmentAlarms.setDeviceID(dataMap.get("deviceID")+"");

                        //对象列表
                        exception.setReasonCode(dataMap.get("alarmCode")+"");
                        exception.setUploadTime(timeStr);
                        List<EquipmentAlarms.Exception> exceptionList = new ArrayList<>();
                        exceptionList.add(exception);
                        //存入对象
                        equipmentAlarms.setExceptionList(exceptionList);
                        //System.out.println("equipmentAlarms::"+equipmentAlarms);

                        JSONObject jsonObject = getJsonObject(equipmentAlarms);
                        ForwardUtils.postAsyncMOM(momIp, "/EAM_AbnormalInformation_Receive", jsonObject);
//                        ForwardUtils.postAsyncMOM("http://127.0.0.1:8083", "/upload", jsonObject);
                    };

                }
                //拼接JsonObject(header+body)
                private JSONObject getJsonObject(Object value) {
                    //JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("body", value);

                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("version","1.0");
                    jsonObject3.put("taskId", UUIDUtil.generate());
                    jsonObject3.put("taskType","EAMR0001-2");
                    JSONObject jsonObject4 = new JSONObject();
                    jsonObject4.put("header",jsonObject3);
                    jsonObject4.putAll(jsonObject2);
                    return jsonObject4;
                }
            });
        }
    }

    //ANDON
    public void andonProcess(List<Equipment> equipmentIdList) {
        for (Equipment equipment : equipmentIdList) {
            //获取所有的属性
            List<String> fieldNames= gatherEquipmentMapper.findAllFieldNameFromEquipment(equipment.getId());
            //System.out.println("fieldNames:"+fieldNames);
            equipmentService.subsFieldListByCode(equipment.getCode(), fieldNames, new EquipmentService.FieldConsumer() {
                @Override
                public void accept(EquipmentData equipmentInfo, String fieldName, Object data) {
                    log.info("获取到设备{},点位{}的值{}", equipmentInfo.getCode(), fieldName, data);
                    String jsonString = JSONObject.toJSONString(data);
                    Map<String, Object> map = null;
                    map = JSON.parseObject(jsonString,Map.class);
                    //System.out.println("数据报文Map："+map.toString());

                    Integer plcForIot =Integer.parseInt(map.get("PLCforIOT")+"");
                    System.out.println("plcForIot的值"+plcForIot);
                    EquipmentClient equipmentClient = httpBuilder.getInstance().getClient(EquipmentClient.class);

                    if (plcForIot == 2) {
                        System.out.println("业务处理:");
                        //数据补充
                        AndonAlarm andonAlarm = setAndon(map);
                        JSONObject jsonObject = getJsonObject(andonAlarm);
                        ForwardUtils.postAsyncMOM(momIp, "/PRD_IssueReceive", jsonObject);
                        //ForwardUtils.postAsyncMOM("http://127.0.0.1:8083", "/upload", jsonObject);
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC",3);

                    } else if (plcForIot == 1) {
                        System.out.println("直接返回");
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC",1);
                    }
                }

                //数据补充
                private AndonAlarm setAndon(Map<String, Object> map){
                    AndonAlarm andon = new AndonAlarm();
                    andon.setSysCode(map.get("sysCode")+"");
                    andon.setProductionLineNo(map.get("productionLine")+"");
                    andon.setWorkCenter(map.get("workCenter")+"");
                    andon.setOprSequenceNo(map.get("oprSequenceNo")+"");
                    andon.setOprSequenceName(map.get("oprSequenceName")+"");
                    andon.setWorkStation(map.get("workStationNo")+"");
                    andon.setWorkStationName(map.get("workStationName")+"");
                    andon.setAndonType(map.get("andonType")+"");
                    andon.setFirstLevelExceptionCategoryName(map.get("firstLevelExceptionCategoryName")+"");
                    andon.setFirstLevelExceptionCategoryCode(map.get("firstLevelExceptionCategoryCode")+"");
                    andon.setExceptionStartDate(map.get("exceptionStartDate")+"");
                    andon.setExceptionCategory(map.get("exceptionCategory")+"");
                    andon.setWipOrderNo(map.get("wipOrderNo")+"");
                    andon.setExceptionDescription(map.get("exceptionDescription")+"");
                    andon.setUrgency(map.get("urgency")+"");
                    andon.setEmployeeNo(map.get("reportedby")+"");
                    andon.setFacility(map.get("facility")+"");
                    return andon;
                }
                //拼接JsonObject(header+body)
                private JSONObject getJsonObject(Object value) {
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("body", value);

                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("version","1.0");
                    jsonObject3.put("taskId", UUIDUtil.generate());
                    jsonObject3.put("taskType","MFGR0005");
                    JSONObject jsonObject4 = new JSONObject();
                    jsonObject4.put("header",jsonObject3);
                    jsonObject4.putAll(jsonObject2);
                    return jsonObject4;
                }
            });
        }
    }
}
