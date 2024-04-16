package infoauto.entity;

import lombok.Data;

import java.util.List;

//加注结果实体
@Data
public class Fill {

    //订单号
    private String wipOrderNo;
    //订单类型
    private String wipOrderType;
    //主机编码
    private String productNo;
    //工序号
    private String oprSequenceNo;
    //工位编号
    private String workStation;
    //序列号
    private String serialNo;
    //组件列表
    private List<MaterialList> materialList;

    @Data
    public static class MaterialList{
        //组件编码
        private String materialNo;
        //组件名称
        private String materialName;
        //数量（加注量）
        private Number quantity;
        //单位
        private String unit;
        //加注日期
        private String refuelTime;
    }

}
