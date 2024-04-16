package infoauto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import infoauto.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by InfoAuto.
 * 采集设备
 * @author : zhuangweizhong
 * @create 2023/9/4 10:24
 */
@Mapper
public interface GatherEquipmentMapper extends BaseMapper {
    //4:报工、5:状态报警、6:ANDON提报

    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '状态报警%'")
//    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where JSON_EXTRACT(tag_ids, '$[0]') = 5")
    List<Equipment> findAllEquipment();

    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like 'ANDON提报%'")
//    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where JSON_EXTRACT(tag_ids, '$[0]') = 6")
    List<Equipment> fillAllAndonEquipment();
    @Select("SELECT field_name FROM equipment_management.hv_em_equipment_field WHERE equipment_id=#{EquipmentId}")
    List<String> findAllFieldNameFromEquipment(String EquipmentId);
}
