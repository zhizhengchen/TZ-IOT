package infoauto.test;

import com.alibaba.fastjson.JSONObject;

import infoauto.anno.MyLog;
import infoauto.dto.Header;
import infoauto.dto.ReportWork;
import infoauto.util.ForwardUtils;
import infoauto.util.ObjectToJSONObjectUtil;
import infoauto.util.Result;
import infoauto.util.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/22 9:11
 */
@RestController
@ResponseBody
@Slf4j
@Api("MOM的接口")
public class TestController {

    /**
     * 向getApplication发送请求
     * @param object
     * @return
     */
    @RequestMapping("/getApplication")
    public Result getApplication(@RequestBody JSONObject object) throws IOException {
        //1、向指定的http发送Post请求，将object参数带上
        ArrayList<String> list=new ArrayList<>();
        list.add("http://127.0.0.1:9000");
        list.add("http://127.0.0.1:9001");
        list.add("http://127.0.0.1:9002");
        ForwardUtils.postAsyncToZKTecos(list,object,"getApplication2");
        JSONObject o=null;
//        String ip=UrlUtils.URLMAP.get("tz-00001");
//        JSONObject o = ForwardUtils.postAwaitResult(ip, "/getApplication2", object);
        /**
         * 转发后接收的响应json值
         */

        return Result.forward(o);
    }

    /**
     * 接收后，处理并反馈接收结果
     * @param object
     * @return
     */

    @RequestMapping("/getApplication2")
    @ApiOperation(value = "测试功能",notes = "测试")

    public Result getApplication2(@RequestBody JSONObject object){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        Object data = jsonObject.put("Data", object);

        jsonObject.put("Data",jsonObject.toJSONString());

        jsonObject2.put("Inputs",jsonObject);
        System.out.println(jsonObject2);

        return Result.success("","00002","ok");
    }
    @RequestMapping("/upload")
    @ApiOperation(value = "接收中控上传测试",notes = "测试")

    public Result getzkJSON(@RequestBody JSONObject object){
        System.out.println(object);
        String taskId = Result.getTaskId(object);
        String version=object.getJSONObject("header").getString("version");
        return Result.success(version,taskId,"ok");
    }
    //测试
    @PostMapping("/test")
    public void test(@RequestBody JSONObject object){
        System.out.println(object);
    }
    //测试
    @PostMapping("/test2")
    public JSONObject test2(){
        String jsonString="{\n" +
                "    \"header\": {\n" +
                "        \"version\": \"1.0\",\n" +
                "        \"taskId\": \"12345678909876543212345678909812\",\n" +
                "        \"taskType\": \"XLZK-WMS-00001\",\n" +
                "        \"code\": \"0\",\n" +
                "        \"msg\": \"\"\n" +
                "    },\n" +
                "    \"returnData\": [\n" +
                "        {\n" +
                "            \"warehouse_no\": \"WH001\",\n" +
                "            \"mat_code\": \"P001\",\n" +
                "            \"pallet_code\": \"T001\",\n" +
                "            \"mat_name\": \"测试物料数据\",\n" +
                "            \"qty\": 1,\n" +
                "            \"length\": \"15m\",\n" +
                "            \"width\": \"4m\",\n" +
                "            \"thick\": \"8cm\",\n" +
                "            \"mat_quality\": \"Q235\",\n" +
                "            \"net_weight\": \"2000\",\n" +
                "            \"steel_type\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"warehouse_no\": \"WH001\",\n" +
                "            \"mat_code\": \"P001\",\n" +
                "            \"pallet_code\": \"T001\",\n" +
                "            \"mat_name\": \"测试物料数据\",\n" +
                "            \"qty\": 1,\n" +
                "            \"length\": \"15m\",\n" +
                "            \"width\": \"4m\",\n" +
                "            \"thick\": \"8cm\",\n" +
                "            \"mat_quality\": \"Q235\",\n" +
                "            \"net_weight\": \"2000\",\n" +
                "            \"steel_type\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        return jsonObject;
    }
}
