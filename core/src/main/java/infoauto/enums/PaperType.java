package infoauto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/15 11:13
 */
@Getter
@AllArgsConstructor
public enum PaperType {

    DEFAULT("Default","默认纸张,根据打印机设置"),CUSTOM("Custom","自定义");
    //打印纸张类型名称
    private String typeName;

    //说明
    private String content;
}
