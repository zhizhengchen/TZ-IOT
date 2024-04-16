package infoauto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/10 15:40
 */
//枚举类
@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS("0","请求成功"),NOTFOUND("404","找不到项目路径"),ERROR("500","程序错误")
    ,PARAMERROR("400","参数错误"),REQUEST_TIME_OUT("408","请求超时"),CONNECTION_TIME_OUT("504","连接超时");
    private String code;
    private String msg;

}
