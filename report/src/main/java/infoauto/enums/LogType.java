package infoauto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/15 17:33
 */
@Getter
@AllArgsConstructor
public enum LogType {
    BUSINESS("business_log","业务日志数据"),
    REQUEST_TIME_OUT("request_time_out_log","请求超时日志");
    private String name;
    private String content;
}
