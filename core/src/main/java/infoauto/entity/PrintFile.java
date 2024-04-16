package infoauto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/15 14:33
 */
@Data
@TableName("print_file")
public class PrintFile {
    //UUID作为主键
    @Column(value = "uuid")
    @TableId(type = IdType.UUID)
    private String uuid;
    //文件地址
    @Column(value = "file_url")
    private String fileUrl;
    //工位编号
    @Column(value = "work_station")
    private String workStation;
    @Column(value = "task_id")
    private String taskId;
    //创建时间
    @Column(value = "create_time")
    private String createTime;
}
