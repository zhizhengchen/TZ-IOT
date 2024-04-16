package infoauto.controller.xl;

import com.alibaba.fastjson.JSONObject;
import infoauto.anno.MyLog;
import infoauto.service.xl.XlZkService;
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
 * 下料产线华工科技接口
 * @author : zhuangweizhong
 * @create 2023/5/30 8:51
 */
@RestController
@RequestMapping("xlHg")
public class HzController {

    @Autowired
    private XlZkService xlZkService;
    @Value("${mom.ip}")
    private String momIp;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${kafka.topic}")
    private String topic;


    @ApiOperation(value = "下料", notes = "生产报工上传")
    @PostMapping("/reportUpload")
    public Result reportUpload(@MyLog @RequestBody JSONObject jsonObject) {
        JSONObject result = ForwardUtils.postAwaitResult(momIp, "PRD_IOTReportWork", jsonObject);
//        KafkaUtils.kafkaSendData(jsonObject,kafkaProducer,topic);
        System.out.println(result);
        return result.getObject("Outputs",Result.class);
    }

    @ApiOperation(value = "下料", notes = "变更执行结果上传")
    @PostMapping("/changeUpload")
    public Result changeUpload(@MyLog @RequestBody JSONObject object){
        String taskId = Result.getTaskId(object);

        ForwardUtils.postAsyncMOM(momIp,"ChangeResultReceive",object);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "临时工艺变更通知结果上传")
    @PostMapping("/temporaryCraftCrUpload")
    public Result temporaryCraftCrUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);

        ForwardUtils.postAsyncMOM(momIp,"TechnologyResultReceive",jsonObject);
        //xlZkService.zkUpload(jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "焊接", notes = "自检结果结果上传")
    @PostMapping("/selfTestResult")
    public Result selfTestResultUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"QMS_ReceiveInspectionResult",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "配盘结果上传")
    @PostMapping("/allocationResult")
    public Result allocationResultUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"LES_AllocationResultFeedBack",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "配送反馈上传")
    @PostMapping("/feedbackUpload")
    public Result distributionFeedbackUpload(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"DistributionFeedback",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "中控到送货单货确认信息上传")
    @PostMapping("/deliveryNote")
    public Result deliveryNoteConfirmation(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"upload",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "下料中控上传车间订单完工入零件库")
    @PostMapping("/completion")
    public Result orderCompletion(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"upload",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "下料中控上传车间订单投料信息")
    @PostMapping("/feedstockMsg")
    public Result orderFeedstockMsg(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"upload",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "下料不合格信息上传")
    @PostMapping("/rejectMsg")
    public Result rejectMsg(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"QMS_ReceiveQualityDefect",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "下料返工返修结果上传")
    @PostMapping("/reworkAndRepair")
    public Result reworkAndRepairResult(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"QMS_ReceiveReworkResult",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "ANDON异常提报")
    @PostMapping("/andonAbnormal")
    public Result andonAbnormalUpload(@MyLog @RequestBody JSONObject jsonObject) {
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp, "PRD_IssueReceive", jsonObject);
        return Result.success("version",taskId,"ok");
    }


    @ApiOperation(value = "下料", notes = "设备状态采集上传")
    @PostMapping("/deviceStatus")
    public Result deviceStatusAcquisition(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EAM_IOTEQUStatus_Receive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "异常报警数据上传")
    @PostMapping("/abnormalWarning")
    public Result abnormalWarning (@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"EAM_AbnormalInformation_Receive",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "关重质量件采集上传")
    @PostMapping("/criticalGather")
    public Result criticalComponentGather(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"PRD_KeyPartsBinding",jsonObject);
        return Result.success("version",taskId,"ok");
    }


    @ApiOperation(value = "下料", notes = "预套料结果上传接口")
    @PostMapping("/jacking")
    public Result jackingUpdate(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"PED_PreNest",jsonObject);
        return Result.success("version",taskId,"ok");
    }

    @ApiOperation(value = "下料", notes = "实际套料结果上传接口")
    @PostMapping("/actualJacking")
    public Result actualJackingUpdate(@MyLog @RequestBody JSONObject jsonObject){
        String taskId = Result.getTaskId(jsonObject);
        ForwardUtils.postAsyncMOM(momIp,"PRD_NestingToSAP",jsonObject);
        return Result.success("version",taskId,"ok");
    }

}
