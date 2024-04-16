package infoauto.service.zp.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hvisions.edge.client.setting.EquipmentClient;
import com.hvisions.edge.equipment.EquipmentData;
import com.hvisions.edge.equipment.EquipmentService;
import com.hvisions.edge.equipment.HttpBuilderInstance;
import infoauto.entity.AssemblySequence;
import infoauto.entity.Equipment;
import infoauto.entity.Fill;
import infoauto.entity.Tighten;
import infoauto.mapper.ZjzlEquipmentMapper;
import infoauto.service.zp.ZkUploadService;
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

@Slf4j
@Service
public class ZkUploadServiceImpl implements ZkUploadService {

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    HttpBuilderInstance httpBuilder;

    @Autowired
    ZjzlEquipmentMapper zjzlEquipmentMapper;


    @Value("${mom.ip}")
    private String momIp;

    /**
     * 装配线中控上传mom等待返回结果
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject zkWaitUpload(JSONObject jsonObject) {

        JSONObject result = ForwardUtils.postAwaitResult(momIp, "upload", jsonObject);
        // Future<Result> resultFuture = transponderService.transponder(jsonObject, url);
        // Result result = resultFuture.get(); //阻塞等待结果
        return result;
    }

    /**
     * 装配线中控上传mom无需等待返回结果
     * @param jsonObject
     */
    @Override
    public void zkUpload(JSONObject jsonObject) {
        ForwardUtils.postAsyncMOM(momIp,"upload",jsonObject);
    }


    //中机中联装配转序上传
    @Override
    public void registerEquipment() {
        //获取所有的设备
        List<Equipment> equipmentIdList = zjzlEquipmentMapper.findEquipment();
        //System.out.println("equipmentIdList:"+equipmentIdList);
        //根据设备id查询该设备下的所有属性
        for (Equipment equipment : equipmentIdList) {
            //获取所有的属性
            List<String> fieldNames= zjzlEquipmentMapper.findAllFieldNameFromEquipment(equipment.getId());
            //System.out.println("fieldNames:"+fieldNames);
            equipmentService.subsFieldListByCode(equipment.getCode(), fieldNames, new EquipmentService.FieldConsumer() {
                @Override
                public void accept(EquipmentData equipmentInfo, String fieldName, Object value) {
                    log.info("获取到设备{},点位{}的值{}", equipmentInfo.getCode(), fieldName, value);
                    EquipmentClient equipmentClient = httpBuilder.getInstance().getClient(EquipmentClient.class);
                    String jsonString = JSONObject.toJSONString(value);
                    System.out.println("json"+jsonString);
                    Map<String, Object> map = null;
                    map = JSON.parseObject(jsonString,Map.class);
                    System.out.println("数据报文Map："+map.toString());

                    Integer plcForIot =Integer.parseInt(map.get("PLCforIOT").toString());
                    System.out.println("PLCforIOT的值"+plcForIot);
                    if (plcForIot == 2) {
                        System.out.println("业务处理:");
                        //数据补充
                        AssemblySequence productReport = setProductReport(map);
                        JSONObject jsonObject = getJsonObject(productReport);
                        //JSONObject result = ForwardUtils.postAwaitResult(momIp, "/PRD_IOTReportWork", jsonObject);
                        JSONObject result = ForwardUtils.postAwaitResult("http://127.0.0.1:8083", "/upload", jsonObject);
                        //System.out.println("result:.............."+result);
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC",3);

                        //将MOM反馈结果写入中控PLC
                        //Integer code = Integer.valueOf(result.getJSONObject("Outputs").getString("code"));
                        //equipmentClient.writeByCode(equipmentInfo.getCode(), "code",code);
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "release",1); //是否允许放行
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "processesCompletion",1); //是否完工
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "qualityManagementInspection",1); //质检是否合格
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "qualitywallInspection",1); //质量墙
                        //获取当前系统时间
                        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/mm/dd hh:mm:ss"));
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "feedbackTime",nowTime); //反馈时间

                    } else if (plcForIot == 1) {
                        System.out.println("直接返回");
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC",1);
                    }
                }

                //数据补充
                private AssemblySequence setProductReport(Map<String, Object> map){
                    AssemblySequence productReport = new AssemblySequence();
                    productReport.setReqSys(map.get("reqSys")+"");
                    productReport.setProductionLineNo(map.get("productionLineNo")+"");
                    productReport.setWipOrderNo(map.get("wipOrderNo")+"");
                    productReport.setSerialNo(map.get("serialNo")+"");
                    productReport.setWorkCenter(map.get("workCenter")+"");
                    productReport.setOprSequenceNo(map.get("oprSequenceNo")+"");
                    productReport.setOprSequenceName("oprSequenceName"+"");
                    productReport.setWorkStation(map.get("workStation")+"");
                    productReport.setWorkStationName(map.get("workStationName")+"");
                    productReport.setRequestTime("requestTime"+"");
                    return productReport;
                }

                //拼接JsonObject(header+body)
                private JSONObject getJsonObject(Object value) {
                    //JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("body", value);

                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("version","1.0");
                    jsonObject3.put("taskId", UUIDUtil.generate());
                    jsonObject3.put("taskType","MFGR0009");

                    JSONObject jsonObject4 = new JSONObject();
                    jsonObject4.put("header",jsonObject3);
                    jsonObject4.putAll(jsonObject2);
                    return jsonObject4;
                }
            });
        }

    }

    //中机中联加注结果上传
    @Override
    public void registerEquipment2() {
        //获取所有的设备
        List<Equipment> equipmentIdList = zjzlEquipmentMapper.findEquipment2();
        //System.out.println("equipmentIdList:"+equipmentIdList);
        //根据设备id查询该设备下的所有属性
        for (Equipment equipment : equipmentIdList) {
            //获取所有的属性
            List<String> fieldNames= zjzlEquipmentMapper.findAllFieldNameFromEquipment(equipment.getId());
            //System.out.println("fieldNames:"+fieldNames);
            equipmentService.subsFieldListByCode(equipment.getCode(), fieldNames, new EquipmentService.FieldConsumer() {
                @Override
                public void accept(EquipmentData equipmentInfo, String fieldName, Object value) {
                    log.info("获取到设备{},点位{}的值{}", equipmentInfo.getCode(), fieldName, value);
                    EquipmentClient equipmentClient = httpBuilder.getInstance().getClient(EquipmentClient.class);
                    String jsonString = JSONObject.toJSONString(value);
                    System.out.println("json"+jsonString);
//                    Map<String, Object> map = null;
                    Map<String, Object> map = null;
                    map = JSON.parseObject(jsonString,Map.class);
                    System.out.println("数据报文Map："+map.toString());

                    Integer plcForIot =Integer.parseInt(map.get("PLCforIOT").toString());
                    System.out.println("PLCforIOT的值"+plcForIot);
                    if (plcForIot == 2) {
                        System.out.println("业务处理:");
                        //数据补充
                        Fill productReport = setProductReport(map);
                        JSONObject jsonObject = getJsonObject(productReport);
                        //JSONObject result = ForwardUtils.postAwaitResult(momIp, "/PRD_IOTReportWork", jsonObject);
                        JSONObject result = ForwardUtils.postAwaitResult("http://127.0.0.1:8083", "/upload", jsonObject);
                        //System.out.println("result:.............."+result);
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC",3);

                        //code是接收MOM的处理结果
                        //Integer code = Integer.valueOf(result.getJSONObject("Outputs").getString("code"));
                        byte code = 0;
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "code",code);

                    } else if (plcForIot == 1) {
                        System.out.println("直接返回");
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC",1);
                    }
                }

                //数据补充
                private Fill setProductReport(Map<String, Object> map){
                    Fill fill = new Fill();
                    fill.setWipOrderNo(map.get("wipOrderNo")+"");
                    fill.setWipOrderType(map.get("wipOrderType")+"");
                    fill.setProductNo(map.get("productNo")+"");
                    fill.setOprSequenceNo(map.get("oprSequenceNo")+"");
                    fill.setWorkStation(map.get("workStation")+"");
                    fill.setSerialNo(map.get("serialNo")+"");

                    List<Fill.MaterialList> list = new ArrayList<>();
                    for (int i = 1; i <= 4; i++) {
                        Fill.MaterialList materials = new Fill.MaterialList();
                        materials.setMaterialNo(map.get("materialNo"+i)+"");
                        materials.setMaterialName(map.get("materialName"+i)+"");
                        materials.setQuantity(Double.valueOf(map.get("quantity"+i)+""));
                        materials.setUnit(map.get("unit"+i)+"");
                        materials.setRefuelTime(map.get("refuelTime"+i)+"");
                        list.add(materials);
                    }
                    fill.setMaterialList(list);
                    return fill;
                }

                //拼接JsonObject(header+body)
                private JSONObject getJsonObject(Object value) {
                    //JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("body", value);

                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("version","1.0");
                    jsonObject3.put("taskId", UUIDUtil.generate());
                    jsonObject3.put("taskType","ENGR0011");

                    JSONObject jsonObject4 = new JSONObject();
                    jsonObject4.put("header",jsonObject3);
                    jsonObject4.putAll(jsonObject2);
                    return jsonObject4;
                }
            });
        }

    }


    //中机中联拧紧结果上传
    @Override
    public void registerEquipment3() {
        //获取所有的设备
        List<Equipment> equipmentIdList = zjzlEquipmentMapper.findEquipment3();
        //System.out.println("equipmentIdList:"+equipmentIdList);
        //根据设备id查询该设备下的所有属性
        for (Equipment equipment : equipmentIdList) {
            //获取所有的属性
            List<String> fieldNames= zjzlEquipmentMapper.findAllFieldNameFromEquipment(equipment.getId());
            //System.out.println("fieldNames:"+fieldNames);
            equipmentService.subsFieldListByCode(equipment.getCode(), fieldNames, new EquipmentService.FieldConsumer() {
                @Override
                public void accept(EquipmentData equipmentInfo, String fieldName, Object value) {
                    log.info("获取到设备{},点位{}的值{}", equipmentInfo.getCode(), fieldName, value);
                    EquipmentClient equipmentClient = httpBuilder.getInstance().getClient(EquipmentClient.class);
                    String jsonString = JSONObject.toJSONString(value);
                    System.out.println("json"+jsonString);
//                    Map<String, Object> map = null;
                    Map<String, Object> map = null;
                    map = JSON.parseObject(jsonString,Map.class);
                    System.out.println("数据报文Map："+map.toString());

                    Integer plcForIot =Integer.parseInt(map.get("PLCforIOT").toString());
                    System.out.println("PLCforIOT的值"+plcForIot);
                    if (plcForIot == 2) {
                        System.out.println("业务处理:");
                        //数据补充
                        Tighten productReport = setProductReport(map);
                        JSONObject jsonObject = getJsonObject(productReport);
                        //JSONObject result = ForwardUtils.postAwaitResult(momIp, "/PRD_IOTReportWork", jsonObject);
                        JSONObject result = ForwardUtils.postAwaitResult("http://127.0.0.1:8083", "/upload", jsonObject);
                        //System.out.println("result:.............."+result);
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC",3);

                        //code是接收MOM的处理结果
                        //Integer code = Integer.valueOf(result.getJSONObject("Outputs").getString("code"));
                        byte code = 0;
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "code",code);


                    } else if (plcForIot == 1) {
                        System.out.println("直接返回");
                        equipmentClient.writeByCode(equipmentInfo.getCode(), "iOTforPLC",1);
                    }
                }


                //数据补充
                private Tighten setProductReport(Map<String, Object> map){
                    Tighten tighten = new Tighten();
                    tighten.setSerialNo(map.get("serialNo")+"");
                    tighten.setWipOrderNo(map.get("wipOrderNo")+"");
                    tighten.setWorkStation(map.get("workStation")+"");
                    tighten.setOprSequenceNo(map.get("oprSequenceNo")+"");
                    tighten.setProductionLine(map.get("productionLine")+"");
                    tighten.setProductNo(map.get("productNo")+"");
                    tighten.setJobNo(map.get("jobNo")+"");
                    tighten.setToolNo(map.get("toolNo")+"");
                    tighten.setToolCnt(map.get("toolCnt")+"");
                    tighten.setEmployeeNo(map.get("employeeNo")+"");

                    List<Tighten.TightenList> list = new ArrayList<>();
                    for (int i =1 ; i <= 16; i++) {
                        Tighten.TightenList tightens = new Tighten.TightenList();
                        tightens.setPsetNo(map.get("psetNo"+i)+"");
                        tightens.setSequenceNo(map.get("sequenceNo"+i)+"");
                        tightens.setQuantity(map.get("quantity"+i)+"");
                        tightens.setActualQuantity(map.get("actualQuantity"+i)+"");
                        tightens.setTightenResult(map.get("tightenResult"+i)+"");
                        tightens.setTorque(map.get("torque"+i)+"");
                        tightens.setTorqueStatus(map.get("torqueStatus"+i)+"");
                        tightens.setActualTorque(map.get("actualTorque"+i)+"");
                        tightens.setUpperTorque(map.get("upperTorque"+i)+"");
                        tightens.setLowerTorque(map.get("lowerTorque"+i)+"");
                        tightens.setAngle(map.get("angle"+i)+"");
                        tightens.setAngleStatus(map.get("angleStatus"+i)+"");
                        tightens.setActualAngle(map.get("actualAngle"+i)+"");
                        tightens.setUpperAngle(map.get("upperAngle"+i)+"");
                        tightens.setLowerAngle(map.get("lowerAngle"+i)+"");
                        tightens.setExecutionTime(map.get("executionTime"+i)+"");
                        list.add(tightens);
                    }
                    tighten.setTightenList(list);

                    return tighten;
                }

                //拼接JsonObject(header+body)
                private JSONObject getJsonObject(Object value) {
                    //JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("body", value);

                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("version","1.0");
                    jsonObject3.put("taskId", UUIDUtil.generate());
                    jsonObject3.put("taskType","ENGR0010");

                    JSONObject jsonObject4 = new JSONObject();
                    jsonObject4.put("header",jsonObject3);
                    jsonObject4.putAll(jsonObject2);
                    return jsonObject4;
                }
            });
        }

    }


}
