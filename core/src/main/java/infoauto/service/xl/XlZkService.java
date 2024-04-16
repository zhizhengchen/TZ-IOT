package infoauto.service.xl;

import com.alibaba.fastjson.JSONObject;

public interface XlZkService {

    JSONObject zkWaitUpload(JSONObject jsonObject);

    void zkUpload(JSONObject jsonObject);

    void momDown(JSONObject jsonObject);
}
