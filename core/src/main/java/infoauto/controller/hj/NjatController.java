package infoauto.controller.hj;

import com.alibaba.fastjson.JSONObject;
import infoauto.anno.MyLog;
import infoauto.util.ForwardUtils;
import infoauto.util.KafkaUtils;
import infoauto.util.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by InfoAuto.
 * 焊接产线南京奥特接口
 * @author : zhuangweizhong
 * @create 2023/5/30 8:45
 */
@RestController
@RequestMapping("hjNjat")
@Slf4j
public class NjatController {

    @Value("${mom.ip}")
    private String momIp;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${kafka.topic}")
    private String  topic;

    @ApiOperation(value = "叫料请求上传", notes = "焊接")
    @PostMapping("/callMaterial")
    public Result callMaterialReq(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"ProductionCall",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "空容器回收请求上传", notes = "焊接")
    @PostMapping("/emptyContainerRecovery")
    public Result emptyContainerRecovery(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EmptyContainer",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "变更执行结果上传", notes = "焊接")
    @PostMapping("/changeUpload")
    public Result changeUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"ChangeResultReceive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "自检结果结果上传", notes = "焊接")
    @PostMapping("/selfTestResult")
    public Result selfTestResultUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"QMS_ReceiveInspectionResult",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "生产报工上传", notes = "焊接")
    @PostMapping("/reportUpload")
    public Result<JSONObject> reportUpload(@MyLog @RequestBody JSONObject jsonObject) {
        String taskId = Result.getTaskId(jsonObject);
        JSONObject result = ForwardUtils.postAwaitResult(momIp, "PRD_IOTReportWork", jsonObject);
//        KafkaUtils.kafkaSendData(jsonObject,kafkaProducer,topic);
        return Result.success("version",taskId,result);
    }

    @ApiOperation(value = "ANDON异常提报", notes = "焊接")
    @PostMapping("/andonAbnormal")
    public Result andonAbnormalUpload(@MyLog @RequestBody JSONObject jsonObject) {
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp, "PRD_IssueReceive", jsonObject);
        return Result.success("version",taskId,"ok");
    }



    @ApiOperation(value = "设备状态采集上传", notes = "焊接")
    @PostMapping("/deviceStatus")
    public Result deviceStatusAcquisition(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EAM_IOTEQUStatus_Receive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "异常报警数据上传", notes = "焊接")
    @PostMapping("/abnormalWarning")
    public Result abnormalWarning (@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EAM_AbnormalInformation_Receive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "临时工艺变更通知结果上传", notes = "焊接")
    @PostMapping("/temporaryChange")
    public Result temporaryChangeResult(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"TechnologyResultReceive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "工序间配送请求上传", notes = "焊接")
    @PostMapping("/processDistribution")
    public Result processDistribution(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"InterprocessDistribution",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "关重件采集上传", notes = "焊接")
    @PostMapping("/criticalGather")
    public Result criticalComponentGather(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"PRD_KeyPartsBinding",jsonObject);
        return Result.success("version",taskId,"ok");
    }
}