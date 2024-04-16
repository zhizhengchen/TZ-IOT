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
//    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where JSON_EXTRACT(tag_ids, '$[0]') = 4")
    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '报工%' ")
    List<Equipment> findAllEquipment();
    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '机加%' ")
    List<Equipment> findAllEquipment2();
    @Select("SELECT id,code FROM equipment_management.hv_em_equipment_setting where name like '测试设备111' ")
    List<Equipment> findAllEquipment11();
    @Select("SELECT field_name FROM equipment_management.hv_em_equipment_field WHERE equipment_id=#{EquipmentId}")
    List<String> findAllFieldNameFromEquipment(String EquipmentId);
}
