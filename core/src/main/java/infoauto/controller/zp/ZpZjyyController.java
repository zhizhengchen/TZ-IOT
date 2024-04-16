package infoauto.controller.zp;

import com.alibaba.fastjson.JSONObject;
import infoauto.anno.MyLog;
import infoauto.util.ForwardUtils;
import infoauto.util.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by InfoAuto.
 * 装配产线中机一院
 * @author : zhuangweizhong
 * @create 2023/5/30 8:53
 */
@RestController
@RequestMapping("zpZjyy")
@Slf4j
public class ZpZjyyController {
    @Value("${mom.ip}")
    private String momIp;
//
//    @ApiOperation(value = "装配", notes = "生产报工上传")
//    @PostMapping("/reportUpload")
//    public JSONObject reportUpload(@MyLog @RequestBody JSONObject jsonObject) {
//        JSONObject result = ForwardUtils.postAwaitResult(momIp, "PRD_IOTReportWork", jsonObject);
//        return result;
//    }

    @ApiOperation(value = "装配", notes = "装配转序校验请求上传")
    @PostMapping("/sectionCheck")
    public JSONObject transferUpload(@MyLog @RequestBody JSONObject jsonObject) {
        return ForwardUtils.postAwaitResult(momIp, "PRD_VerifyLine", jsonObject);
    }
    @ApiOperation(value = "装配", notes = "拧紧结果上传")
    @PostMapping("/screwDown")
    public Result tightenResultUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"TightenResultReceive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "装配", notes = "加注信息上传")
    @PostMapping("/fillInformation")
    public Result fillMessageUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"RefuelInfoReceive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    }
