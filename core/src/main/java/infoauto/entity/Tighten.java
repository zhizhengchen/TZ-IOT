package infoauto.entity;

import lombok.Data;

import java.util.List;

//拧紧结果实体
@Data
public class Tighten {

    //工厂编号
    private String facility;
    //产线编号
    private String productionLine;
    //拧紧工位编号
    private String workStation;
    //工单号
    private String wipOrderNo;
    //序列号
    private String serialNo;
    //工序号
    private String oprSequenceNo;
    //主机编码
    private String productNo;
    //JOB号
    private String jobNo;
    //工具编号
    private String toolNo;
    //工具数量
    private String toolCnt;
    //人员编号
    private String employeeNo;
    //拧紧程序列表
    private List<TightenList> TightenList;

    //拧紧程序列表
    @Data
    public static class TightenList{
        //Pset编号
        private String psetNo;
        //拧紧顺序
        private String sequenceNo;
        //拧紧数量
        private String quantity;
        //拧紧数量实际
        private String actualQuantity;
        //拧紧结果
        private String tightenResult;
        //扭矩目标值
        private String torque;
        //扭矩状态
        private String torqueStatus;
        //扭矩实际值
        private String actualTorque;
        //扭矩上限
        private String upperTorque;
        //扭矩下限
        private String lowerTorque;
        //角度目标值
        private String angle;
        //角度状态
        private String angleStatus;
        //角度实际值
        private String actualAngle;
        //角度上限
        private String upperAngle;
        //角度下限
        private String lowerAngle;
        //执行时间
        private String executionTime;

    }
}
