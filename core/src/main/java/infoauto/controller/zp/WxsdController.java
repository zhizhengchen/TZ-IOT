package infoauto.controller.zp;

import com.alibaba.fastjson.JSONObject;
import infoauto.anno.MyLog;
import infoauto.util.ForwardUtils;
import infoauto.util.KafkaUtils;
import infoauto.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by InfoAuto.
 * 装配产线无锡顺达接口
 * @author : zhuangweizhong
 * @create 2023/5/30 8:53
 */
@RestController
@RequestMapping("/zpWxsd")
@Api("装配产线无锡顺达接口")
public class WxsdController {


    @Value("${mom.ip}")
    private String momIp;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${kafka.topic}")
    private String topic;

    @ApiOperation(value = "装配", notes = "生产报工上传")
    @PostMapping("/reportUpload")
    public JSONObject reportUpload(@MyLog @RequestBody JSONObject jsonObject) {
        JSONObject result = ForwardUtils.postAwaitResult(momIp, "PRD_IOTReportWork", jsonObject);
//        KafkaUtils.kafkaSendData(jsonObject,kafkaProducer,topic);
        return result;
    }

    @ApiOperation(value = "装配", notes = "装配转序校验请求上传")
    @PostMapping("/sectionCheck")
    public JSONObject transferUpload(@MyLog @RequestBody JSONObject jsonObject) {
        return ForwardUtils.postAwaitResult(momIp, "PRD_VerifyLine", jsonObject);
    }

    @ApiOperation(value = "装配", notes = "变更执行结果上传")
    @PostMapping("/changeUpload")
    public Result changeUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        //long s = System.currentTimeMillis();
        ForwardUtils.postAsyncMOM(momIp,"ChangeResultReceive",jsonObject);
        //System.out.println("时.....长:"+(System.currentTimeMillis()-s));
        return Result.success("version",taskId,"ok");
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

    @ApiOperation(value = "装配", notes = "临时工艺变更通知结果上传")
    @PostMapping("/temporaryChange")
    public Result temporaryCraftCrUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"TechnologyResultReceive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    //PLC采集
    @ApiOperation(value = "装配", notes = "设备状态采集上传")
    @PostMapping("/deviceStatus")
    public Result deviceStatusAcquisition(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EAM_IOTEQUStatus_Receive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    //PLC采集
    @ApiOperation(value = "装配", notes = "异常报警数据上传")
    @PostMapping("/abnormalWarning")
    public Result abnormalWarning (@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EAM_AbnormalInformation_Receive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

}
