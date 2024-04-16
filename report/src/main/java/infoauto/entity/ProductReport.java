package infoauto.entity;

import lombok.Data;

//生产报工实体
@Data
public class ProductReport {
    //请求ID
    private String reqId;
    //系统编码
    private String reqSys;
    //工厂编号
    private String facility;
    //工单号
    private String wipOrderNo;
    //工序号
    private String oprSequenceNo;
    //实际开始时间
    private String actualStartDate;
    //实际结束时间
    private String actualCompleteDate;
    //工作中心
    private String workCenter;
    //工位
    private String workStation;
    //设备编号 对应PLC采集的deviceID
    private String equipment;
    //工序报工类型
    private String progressType;
    //计量单位
    private String unit;
    //完工数量
    private Integer completedQuantity;
    //工序是否完工
    private boolean ifCompleted;
    //报工员工号
    private String employeeNo;
    //产品物料号
    private String productNo;
    //产品序列号
    private String serialNo;
    //预留参数1
    private String paramRsrv1;
    //预留参数2
    private String paramRsrv2;
    //预留参数3
    private String paramRsrv3;
    //预留参数4
    private String paramRsrv4;
    //预留参数5
    private String paramRsrv5;
}
