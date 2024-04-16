package infoauto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Created by InfoAuto.
 * 打印参数，根据工位处理
 * @author : zhuangweizhong
 * @create 2023/8/22 11:30
 */
@Data
@TableName("print_parameter_config")
public class PrintParameterConfig {
    //打印工位编号
    @TableId()
    @Column(value = "work_station")
    private String workStation;
    //宽度
    @Column(value = "width")
    private Double width;
    //高度
    @Column(value = "height")
    private Double height;
    //左边距
    @Column(value = "margin_left")
    private Double marginLeft;
    //右边距
    @Column(value = "margin_right")
    private Double marginRight;
    //上边距
    @Column(value = "margin_top")
    private Double marginTop;
    //下边距
    @Column(value = "margin_bottom")
    private Double marginBottom;
}
