package infoauto.util;

import java.util.UUID;

/**
 * Created by InfoAuto.
 *UUID工具类
 * @author : zhuangweizhong
 * @create 2023/8/2 16:17
 */
public class UUIDUtil {

    //32位UUID生成
    public static String generate(){
       return UUID.randomUUID().toString().replaceAll("-","");
    }
}
