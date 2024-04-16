package infoauto.service.zp;

import com.alibaba.fastjson.JSONObject;
import infoauto.util.Result;

import java.util.concurrent.ExecutionException;

public interface ZkUploadService {

    JSONObject zkWaitUpload(JSONObject jsonObject);

    void zkUpload(JSONObject jsonObject);

    //启动注入设备实现
    void registerEquipment();
    void registerEquipment2();
    void registerEquipment3();
}
