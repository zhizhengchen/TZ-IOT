package infoauto.util;

import com.alibaba.fastjson.JSONObject;
import infoauto.dto.Body;
import infoauto.dto.Header;

/**
 * Created by InfoAuto.
 * 对象转JSON对象工具类
 * @author : zhuangweizhong
 * @create 2023/8/2 16:01
 */
public class ObjectToJSONObjectUtil {

    public static JSONObject ObjectToJSONObject(Header header, Body body){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("header",header);
        jsonObject.put("body",body);
        return jsonObject;
    }
}
