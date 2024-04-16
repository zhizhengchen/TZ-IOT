package infoauto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String taskId;  //任务ID
    private String code;   //返回状态码
    private String msg;    //返回消息
    private String returnData;   //返回结果集

    public static Result error(String code){
        Result result = new Result();
        result.code = code;
        return result;
    }

    public static Result success(String taskId,String msg, String date){
        Result result = new Result();
        result.taskId = taskId;
        result.code = "200";
        result.msg = msg;
        result.returnData = date;
        return result;
    }

}
