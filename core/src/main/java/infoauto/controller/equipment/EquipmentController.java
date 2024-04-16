package infoauto.controller.equipment;

import com.alibaba.fastjson.JSONObject;
import infoauto.util.ForwardUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/8/11 13:15
 */
@RestController
@RequestMapping("/equipment")
@Slf4j
@Api("设备相关接口")
public class EquipmentController {

    @Value("${mom.ip}")
    private String momIp;

    @ApiOperation(value = "设备状态采集", notes = "设备状态采集上传")
    @PostMapping("/EAMR0001")
    public JSONObject equipmentStatus(@RequestBody JSONObject jsonObject) {
        JSONObject result = ForwardUtils.postAwaitResult(momIp, "EAM_IOTEQUStatus_Receive", jsonObject);
        return result;
    }
    @ApiOperation(value = "设备报警信息上传", notes = "设备报警信息上传")
    @PostMapping("/EAMR0002")
    public JSONObject equipmentAlarms(@RequestBody JSONObject jsonObject) {
        JSONObject result = ForwardUtils.postAwaitResult(momIp, "EAM_AbnormalInformation_Receive", jsonObject);
        return result;
    }
}
