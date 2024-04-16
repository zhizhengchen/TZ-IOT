package infoauto.entity;

import lombok.Data;

//装配转序校验请求上传实体
@Data
public class AssemblySequence {

    //系统编码
    private String reqSys;
    //工厂编码
    private String facility;
    //产线编号
    private String productionLineNo;
    //工单号
    private String wipOrderNo;
    //序列号
    private String serialNo;
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
    //请求时间
    private String requestTime;
    //预留参数1
    private String paramRsrv1;
    private String paramRsrv2;
    private String paramRsrv3;
    private String paramRsrv4;
    private String paramRsrv5;

}
