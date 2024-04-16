package infoauto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2024/1/29 16:15
 */
@Data
@TableName("business_log")
public class Logs {
    //任务ID
    @Column(value = "taskId")
    private String taskId;
    //版本号
    @Column(value = "version")
    private String version;
    //任务类型
    @Column(value = "taskType")
    private String taskType;
    //调用时间
    @Column(value = "time")
    private String time;
    //调用数据
    @Column(value = "data")
    private String data;
}
