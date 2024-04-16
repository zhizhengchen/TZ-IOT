package infoauto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by InfoAuto.
 *消息头
 * @author : zhuangweizhong
 * @create 2023/8/2 15:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Header {
    //协议版本
    private String version;
    //消息ID
    private String taskId;
    //接口类型
    private String taskType;

}
