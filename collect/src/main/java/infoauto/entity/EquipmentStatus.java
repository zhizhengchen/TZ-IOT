package infoauto.entity;

import lombok.Data;

//设备状态实体类
@Data
public class EquipmentStatus {

    //设备ID
    public String deviceID;
    //属性编号
    public String propertyId;
    //数值
    public String value;
    //变更时间
    public String updateTime;
    //预留参数1
    public String paramRsrv1;
    //预留参数2
    public String paramRsrv2;
}
