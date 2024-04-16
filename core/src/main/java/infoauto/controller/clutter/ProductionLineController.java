package infoauto.controller.clutter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import infoauto.service.clutter.impl.UrlServiceImpl;
import infoauto.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

/**
 * Created by InfoAuto.
 *  产线与ip维护的相关的接口
 * @author : zhuangweizhong
 * @create 2023/5/30 8:34
 */
@Controller("/productionLine")
public class ProductionLineController {
    @Autowired
    UrlServiceImpl urlService;

    /**
     * 根据供应商批量更新状态
     * @param object
     * @return
     */
//    public Result updateStateForSupliers(@RequestBody JSONObject object){
//        int state=object.getInteger("state");
//        JSONArray jsonArray = object.getJSONArray("suplier");
//        ArrayList<String> supliers=new ArrayList<>();
//
//        for(int i=0;i<jsonArray.size();i++){
//            supliers.add((String)jsonArray.get(i));
//        }
//        int result = urlService.updateStateForSupplier(state, supliers);
//
//        return Result.success("","",result);
//    }
}
