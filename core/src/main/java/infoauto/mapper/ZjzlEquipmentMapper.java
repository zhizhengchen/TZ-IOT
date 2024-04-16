package infoauto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import infoauto.entity.Equipment;
import infoauto.entity.ProductOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by InfoAuto.
 * 采集设备
 * @author : zhuangweizhong
 * @create 2023/9/4 10:24
 */
public interface ZjzlEquipmentMapper extends BaseMapper {
    //4:报工、5:状态报警、6:ANDON提报
    //@Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where JSON_EXTRACT(tag_ids, '$[0]') = 4")
    //工单下发
    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '工单%' ")
    List<Equipment> findEquipment1();

    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '转序%' ")
    List<Equipment> findEquipment();

    //获取加注机位设备
    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '加注%' ")
    List<Equipment> findEquipment2();

    //获取加注机位设备
    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '拧紧%' ")
    List<Equipment> findEquipment3();

    @Insert("INSERT into product_order values (#{taskId},#{wipOrderNo},#{wipOrderType})")
    void addData(ProductOrder productOrder);
//    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '报工%' ")
//    List<Equipment> findAllEquipment();

    //获取设备所有属性
    @Select("SELECT field_name FROM equipment_management.hv_em_equipment_field WHERE equipment_id=#{EquipmentId}")
    List<String> findAllFieldNameFromEquipment(String EquipmentId);

}
