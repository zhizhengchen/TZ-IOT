package infoauto.service.xl.impl;

import com.alibaba.fastjson.JSONObject;
import infoauto.service.xl.XlZkService;
import infoauto.util.ForwardUtils;
import infoauto.util.UrlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class XlZkServiceImpl implements XlZkService {

    @Value("${mom.ip}")
    private String momIp;


    @Override
    public JSONObject zkWaitUpload(JSONObject jsonObject) {
        JSONObject result = ForwardUtils.postAwaitResult(momIp, "upload", jsonObject);
        return result;
    }

    @Override
    public void zkUpload(JSONObject jsonObject) {
        ForwardUtils.postAsyncMOM(momIp,"upload",jsonObject);
    }

    @Override
    public void momDown(JSONObject jsonObject) {
        //获取装配产线url
        String url = UrlUtils.getUrlConverterString(jsonObject.getJSONObject("body").getString("productionLine"));
        ForwardUtils.postAsyncMOM(url,"load",jsonObject);
    }
}
