package infoauto.entity;


import lombok.Data;

import java.util.List;

//ANDON提报实体类
@Data
public class AndonAlarm {
    //异常报警ID
    private String abnormalAlarmID;
    //系统编码
    private String sysCode;
    //工厂编号
    private String facility;
    //产线编号
    private String productionLineNo;
    //工作中心
    private String workCenter;
    //工序号
    private String oprSequenceNo;
    //工序名称
    private String oprSequenceName;
    //工位编号
    private String workStation;
    //工位名称
    private String workStationName;
    //安灯类型
    private String andonType;
    //异常类型名称
    private String firstLevelExceptionCategoryName;
    //异常类型代码
    private String firstLevelExceptionCategoryCode;
    //异常类别
    private String exceptionCategory;
    //异常提报时间
    private String exceptionStartDate;
    //工单号
    private String wipOrderNo;
    //异常描述
    private String exceptionDescription;
    //紧急程度
    private String urgency;
    //图片
    private List<String> picture;
    //提报人
    private String employeeNo;
    //预留参数1
    private String paramRsrv1;
    //预留参数2
    private String paramRsrv2;

}
