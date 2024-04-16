package infoauto.service.zp;

import com.alibaba.fastjson.JSONObject;

//处理下发中机中联业务接口
public interface MomDownService {

    void momDownZJZL(String lineCode) throws InterruptedException;

    void momDownBinding(String productionLineNo) throws InterruptedException;



}
