package infoauto.util;

import com.alibaba.fastjson.JSONObject;
import infoauto.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/10 15:40
 */

//lombok的注解使用
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    private String version;
    private String code;
    private String msg;
    private String taskId;
    private T returnData;

    public Result(T returnData){
        this.returnData=returnData;
    }
    //封装成功结果集，静态方法
    public static <T> Result<T> success(String version,String taskId,T returnData){
        return new Result<>(version,ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMsg(),taskId,returnData);
    }
    //封装失败结果集合
    public static <T> Result<T> error(String version,ResponseEnum responseEnum,String taskId,T returnData){
        return new Result<>(version,responseEnum.getCode(),responseEnum.getMsg(),taskId,returnData);
    }
    //获取接口任务ID
    public static String getTaskId(JSONObject object){
        return object.getJSONObject("header").getString("taskId");
    }

    //获取接口产线编号对应的url
    public static String getUrl(JSONObject object){
        String url = UrlUtils.getUrlConverterString(object.getJSONObject("body").getString("productionLine"));
        return url;
    }

    public static Result<Object> aaa(String version, String code, String msg, String taskId){
        Result<Object> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setVersion(version);
        result.setTaskId(taskId);
        result.setReturnData("aaaa");
        return result;
    };
    /**
     * 接收转发的值
     */
    public static Result forward(JSONObject o){
        return new Result(o);
    }

}
