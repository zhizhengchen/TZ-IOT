package infoauto.dto;

import infoauto.entity.Logs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2024/2/21 14:39
 */
@AllArgsConstructor
@Data
@Accessors
public class LogsDTO {
    //页码
    private Integer page;
    //最大页数
    private Integer pageMax;
    //排序方向，正序或倒序
    private String direction;
    //数据集合
    private List<Logs> logsList;
    //响应状态码
    private String code;
}
