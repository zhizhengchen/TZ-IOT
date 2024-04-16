package infoauto.controller.jj;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2024/1/15 15:29
 */

import com.alibaba.fastjson.JSONObject;
import infoauto.anno.MyLog;
import infoauto.util.ForwardUtils;
import infoauto.util.KafkaUtils;
import infoauto.util.Result;
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
 * 机加产线江苏高精接口
 * @author : zhuangweizhong
 * @create 2023/5/30 8:47
 */
@RestController
@RequestMapping("jjjcgj")
public class JcgjController {

    @Value("${mom.ip}")
    private String momIp;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${kafka.topic}")
    private String topic;

    @ApiOperation(value = "机加", notes = "单机机床数采")
    @PostMapping("/dataCollection")
    public Result machineToolDataCollection(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "机加", notes = "生产报工上传")
    @PostMapping("/reportUpload")
    public Result<JSONObject> reportUpload(@MyLog @RequestBody JSONObject jsonObject) {
        String taskId = Result.getTaskId(jsonObject);
        JSONObject result = ForwardUtils.postAwaitResult(momIp, "PRD_IOTReportWork", jsonObject);
//        KafkaUtils.kafkaSendData(jsonObject,kafkaProducer,topic);
        return Result.success("version",taskId,result);
    }

    @ApiOperation(value = "机加", notes = "空容器回收请求上传")
    @PostMapping("/emptyContainerRecovery")
    public Result emptyContainerRecovery(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EmptyContainer",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "机加", notes = "自检结果结果上传")
    @PostMapping("/selfTestResult")
    public Result selfTestResultUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"QMS_ReceiveInspectionResult",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "机加", notes = "变更执行结果上传")
    @PostMapping("/changeUpload")
    public Result changeUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"ChangeResultReceive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "机加", notes = "ANDON异常提报")
    @PostMapping("/andonAbnormal")
    public Result andonAbnormalUpload(@MyLog @RequestBody JSONObject jsonObject) {
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp, "PRD_IssueReceive", jsonObject);
        return Result.success("version",taskId,"ok");
    }


    @ApiOperation(value = "机加", notes = "设备状态采集上传")
    @PostMapping("/deviceStatus")
    public Result deviceStatusAcquisition(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EAM_IOTEQUStatus_Receive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "机加", notes = "异常报警数据上传")
    @PostMapping("/abnormalWarning")
    public Result abnormalWarning (@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EAM_AbnormalInformation_Receive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "机加", notes = "临时工艺变更通知结果上传")
    @PostMapping("/temporaryChange")
    public Result temporaryChangeResult(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"TechnologyResultReceive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "机加", notes = "工序间配送请求上传")
    @PostMapping("/processDistribution")
    public Result processDistribution(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"InterprocessDistribution",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "机加", notes = "关重质量件采集上传")
    @PostMapping("/criticalGather")
    public Result criticalComponentGather(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"PRD_KeyPartsBinding",jsonObject);
        return Result.success("version",taskId,"ok");
    }
}
