package infoauto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/7/14 13:50
 */
@Data
@TableName("supplier_controller")
public class SupplierController {

    //产线表的产线id
    @Column(value = "production_line_url_id")
    private Integer productionLineUrlId;
    //MOM的接口路径
    @Column(value = "mom_controller")
    private String momControllerKey;
    //产线中控的具体controller
    @Column(value = "controller")
    private String controller;
    //接口名称
    @Column(value = "explain")
    private String explain;
}
