package infoauto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Component;

@Component
@Data
@TableName("product_order")
public class ProductOrder {

    //消息ID
    @Column(value = "task_id")
    private String taskId;
    //工单号
    @Column(value = "wip_order_no")
    private String wipOrderNo;
    //工单类型
   @Column(value = "wip_order_type")
    private String wipOrderType;
    //工厂
    /* @Column(value = "task_id")
    private String facility;
    //工单物料编码
    @Column(value = "create_time")
    private String productNo;
    //工单物料描述
    @Column(value = "create_time")
    private String productName;
    //计量单位
    @Column(value = "create_time")
    private String unit;
    //数量
    @Column(value = "create_time")
    private Float quantity;
    //计划开始时间
    @Column(value = "create_time")
    private String scheduledStartDate;
    //计划结束时间
    @Column(value = "create_time")
    private String scheduledCompleteDate;
    //工单状态
    @Column(value = "create_time")
    private String progressStatus;
    //序列号
    @Column(value = "create_time")
    private String serialNo;
    //机型月顺序号
    @Column(value = "create_time")
    private String monthlySequenceNo;
    //机型
    @Column(value = "create_time")
    private String productType;
    //工单文本
    @Column(value = "create_time")
    private String textLine;

    /////工序明细oprSequenceNoList[{}]///////////////////////////////////////////////////////////////////////////////////

    //产线编号
    @Column(value = "create_time")
    private String productionLineNo;

    //工作中心
    @Column(value = "create_time")
    private String workCenter;
    //工作中心描述
    @Column(value = "create_time")
    private String workCenterName;
    //工序号
    @Column(value = "create_time")
    private String oprSequenceNo;
    //前工序
    @Column(value = "create_time")
    private String preOprSequenceNo;
    //后工序
    @Column(value = "create_time")
    private String postOprSequenceNo;
    //工序名称
    @Column(value = "create_time")
    private String oprSequenceName;
    //工序类型
    @Column(value = "create_time")
    private String oprSequenceType;
    //工位
    @Column(value = "create_time")
    private String workStation;
    //工位描述
    @Column(value = "create_time")
    private String workStationName;
    //工序状态
//    @Column(value = "create_time")
//    private String progressStatus;
    //工序数量
//    @Column(value = "create_time")
//    private Float quantity;
    //自检
    @Column(value = "create_time")
    private Boolean selfCheck;
    //专检
    @Column(value = "create_time")
    private Boolean specialInspection;
    //质量门
    @Column(value = "create_time")
    private Boolean qualityGate;
    //质量墙
    @Column(value = "create_time")
    private Boolean qualityWall;

    /////组件列表/componentList//////////////////////////////////////////////////////////////////////////////////////////

    //预留号
    @Column(value = "create_time")
    private String reserveNo;
    //预留行项目号
    @Column(value = "create_time")
    private String reserveLineNo;
    //组件物料编码
    @Column(value = "create_time")
    private String materialNo;
    //组件物料描述
    @Column(value = "create_time")
    private String materialName;
    //组件物料简码
    @Column(value = "create_time")
    private String materialAlias;
    //组件数量
//    @Column(value = "create_time")
//    private string quantity;
    //单位
//    @Column(value = "create_time")
//    private String unit;
    //加注物料标识
    @Column(value = "create_time")
    private Boolean fuel;
    //质量采集件
    @Column(value = "create_time")
    private Boolean qualityScanCode;
    //关重件
    @Column(value = "create_time")
    private Boolean keyFlag;

    //componentSeriaNo/组件序列号/批次号清单明细////////////////////////////////////////////////////////////////////////////
    //物料批次号
    @Column(value = "create_time")
    private String materialBatchNo;
    //批次数量
    @Column(value = "create_time")
    private Float materialBatchQuantity;
    //物料条码号
    @Column(value = "create_time")
    private String materialBarcodeNo;
    //条码数量
    @Column(value = "create_time")
    private Float materialBarcodeQuantity;*/

}
