package infoauto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Created by InfoAuto.
 * 打印服务表
 * @author : zhuangweizhong
 * @create 2023/6/15 12:20
 */
@Data
@TableName("print_service")
public class PrintService {
    //打印服务ID
    @TableId(type = IdType.AUTO)
    private Integer id;
    //打印工位编号
    @Column(value = "work_station")
    private String workStation;
    //打印服务名称
    @Column(value = "printer_name")
    private String printerName;
    //打印服务启用状态
    @Column(value = "state")
    private Integer state;
}
