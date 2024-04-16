package infoauto.service;

import infoauto.entity.Equipment;

import java.util.List;

/**
 * Created by InfoAuto.
 *  采集设备服务
 * @author : zhuangweizhong
 * @create 2023/9/4 10:17
 */
public interface GatherEquipmentService {

    //获取所有设备id
    List<Equipment> findAllEquipment();
    //获取设备的所有属性FieldName
    List<String> findAllFieldNameFromEquipment(String EquipmentId);
    //启动注入设备实现
    void registerEquipment();

}
