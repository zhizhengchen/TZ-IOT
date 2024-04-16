package infoauto.controller.mom;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.Forest;
import com.dtflys.forest.utils.ForestDataType;
import infoauto.anno.MyLog;
import infoauto.enums.LogType;
import infoauto.enums.ResponseEnum;
import infoauto.service.clutter.UrlService;
import infoauto.service.zp.MomDownService;
import infoauto.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by InfoAuto.
 * 上游系统MOM接口
 *
 * @author : zhuangweizhong
 * @create 2023/5/30 8:32
 */
@RestController
@RequestMapping
@Api("MOM的接口")
public class MOMController {


    //下料中控IP
    @Value("${materials.ip}")
    private String materialsId;

    //中机中联IP
//    @Value("${zjzl.productLineNo}")
//    private List<String> zjzlProductLineNo;
    //中机中联IP
//    @Value("${zjzl.productLineNo}")
//    private String zjzlProductLineNo;
    @Value("${printserver.ip}")
    private String printserverIp;

    //机加江苏高精的ip
    @Value("${jj.jcgj.ip}")
    private String jcgjIp;

    @Autowired
    private TaskExecutor taskExecutor;

    @Resource
    private UrlService urlService;

//    public final static String TOKEN = "?access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhcHBpZCI6IkxOMjAyMTEyMDcwMDAwMDMiLCJzZWNyZXQiOiJkY2MxZDAxOGVhYmU0ODMxYjJkNmJjYzQwYTMyYTE5YSIsImlhdCI6MTYzODg2NzU1MywidGltZXN0YW1wIjoiMTYzODg2NzU1MzgyMSJ9.6XxiWsC-9_83cn6QScnAjabkWczh-CpXXq7eEcaSACQ";

    /**
     * 生产建模基础数据下发功能
     *
     * @param object
     * @return
     */
    @ApiOperation(value = "生产建模基础数据下发功能", notes = "生产建模基础数据")
    @PostMapping("/ENGG0001")
    public Result productionModelBasicDistribute(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        Collection<String> values = UrlUtils.URLMAP.values();
        List<String> list = new ArrayList<>();
        for (Object v : values) {
            list.add(v.toString());
        }
        list.add(jcgjIp);
        list = list.stream().distinct().collect(Collectors.toList());
        //手动添加江苏高精的点位

        //以下是测试
//            ArrayList<String> test = new ArrayList<>();
//            test.add("http://10.79.128.130:8080/iotApi");
//            ForwardUtils.postAsyncToZKBaseTecos(test,object,"product");
        ForwardUtils.postAsyncToZKBaseTecos(list, object, "product");
        return Result.success(version, taskId, "下发成功");

    }

    /**
     * 人员基础数据下发
     *
     * @param object
     * @return
     */
    @ApiOperation(value = "人员基础数据下发功能", notes = "人员基础数据")
    @PostMapping("/ENGG0002")
    public Result personBasicDistribute(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //人员基础数据是全量下发，所以代码如下
        //必须保证数据表中的数据不为空，否则报异常
        Collection<String> values = UrlUtils.URLMAP.values();
        List<String> list = new ArrayList<>();
        for (Object v : values) {
            list.add(v.toString());
        }
        list.add(jcgjIp);
        //测试
//             ForwardUtils.postAsyncZK("http://10.79.128.130:8080/iotApi","person",object);
        list = list.stream().distinct().collect(Collectors.toList());
        //手动添加江苏高精
        ForwardUtils.postAsyncToZKBaseTecos(list, object, "person");
        return Result.success(version, taskId, "ok");
    }

    @ApiOperation(value = "拧紧JOB下发功能", notes = "拧紧JOB下发")
    @PostMapping("/ENGG0008")
    public Result tightenJob(@RequestBody JSONObject object) {
        String version = object.getJSONObject("header").getString("version");
        String taskId = object.getJSONObject("header").getString("taskId");
        JSONArray productionLineNoArray = object.getJSONObject("body").getJSONArray("productionLineNoArray");
        if (productionLineNoArray != null && productionLineNoArray.size() > 0) {
            List<String> urls = new ArrayList<>();
            for (int i = 0; i < productionLineNoArray.size(); i++) {
                String url = UrlUtils.URLMAP.get((String) productionLineNoArray.get(i));
                if (url != null) {
                    urls.add(url);
                }
            }
            urls = urls.stream().distinct().collect(Collectors.toList());
            ForwardUtils.postAsyncToZKTecos(urls, object, "tightenJob");
            return Result.success(version, taskId, "ok");
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号组下的所有ip，请检查产线编号是否有问题");
        }
    }

    @ApiOperation(value = "加注工艺参数下发功能", notes = "加注工艺参数下发")
    @PostMapping("/ENGG0009")
    public Result fillParameterDown(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        JSONArray productionLineNoArray = object.getJSONObject("body").getJSONArray("productionLineNoArray");
        if (productionLineNoArray != null && productionLineNoArray.size() > 0) {
            List<String> urls = new ArrayList<>();
            for (int i = 0; i < productionLineNoArray.size(); i++) {
                String url = UrlUtils.URLMAP.get((String) productionLineNoArray.get(i));
                if (url != null) {
                    urls.add(url);
                }
            }
            urls = urls.stream().distinct().collect(Collectors.toList());
            ForwardUtils.postAsyncToZKTecos(urls, object, "fillParameter");
            return Result.success(version, taskId, "ok");
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号组下的所有ip，请检查产线编号是否有问题");
        }
//        int count=0;
//        List<String> list=new ArrayList<>();
//        if (productionLineNoArray!=null&&productionLineNoArray.size()>0){
//            for (int i=0;i<productionLineNoArray.size();i++){
//                String productionLineIp = UrlUtils.URLMAP.get((String) productionLineNoArray.get(i));
//                if (StringUtils.isNotEmpty(productionLineIp)){
//                    list.add(productionLineIp);
//                    count++;
//                }
//            } if (list.size()>0&&productionLineNoArray.size()==count){
////              ForwardUtils.postAsyncZK("http://10.73.147.6:30000","api/iottozk/fillParameter",object);
//                ForwardUtils.postAsyncToZKTecos(list,object,"fillParameter");
//                return Result.success(version,taskId,"ok");
//            }else {
//                return Result.error(version,ResponseEnum.PARAMERROR,taskId,"找不到此产线编号组下的所有ip，请检查产线编号是否有问题");
//            }
//        }
//        return Result.error(version,ResponseEnum.PARAMERROR,taskId,"产线编号数组为空");
    }

    /**
     * 物料主数据下发
     *
     * @param object
     * @return
     */
    @ApiOperation(value = "物料主数据下发功能", notes = "物料主数据")
    @PostMapping("/ENGG0003")
    public Result materialMasterDistribute(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //物料主数据是全量下发，所以代码如下
        //必须保证数据表中的数据不为空，否则报异常
        Collection<String> values = UrlUtils.URLMAP.values();
        List<String> list = new ArrayList<>();
        for (Object v : values) {
            list.add(v.toString());
        }
        list.add(jcgjIp);
        list = list.stream().distinct().collect(Collectors.toList());

//            //以下是测试
//            ArrayList<String> test = new ArrayList<>();
//            test.add("http://10.79.130.68:5003/NAEC");
//            ForwardUtils.postAsyncToZKBaseTecos(test, object, "material");
        ForwardUtils.postAsyncToZKBaseTecos(list, object, "material");
        return Result.success(version, taskId, "ok");
    }

    /**
     * 设备台账基础数据下发
     *
     * @param object
     * @return
     */
    @ApiOperation(value = "设备台账基础数据下发功能", notes = "设备台账基础数据")
    @PostMapping("/EAMG0001")
    public Result equipmentLedgerDistribute(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //物料主数据是全量下发，所以代码如下
        //必须保证数据表中的数据不为空，否则报异常
        Collection<String> values = UrlUtils.URLMAP.values();
        List<String> list = new ArrayList<>();
        list.add(jcgjIp);
        for (Object v : values) {
            list.add(v.toString());
        }
        list = list.stream().distinct().collect(Collectors.toList());
//以下是测试
//            ArrayList<String> test = new ArrayList<>();
//            test.add("http://10.79.128.130:8080/iotApi");
//
//            ForwardUtils.postAsyncToZKBaseTecos(test,object,"device");

        ForwardUtils.postAsyncToZKBaseTecos(list, object, "device");
        return Result.success(version, taskId, "ok");
    }

    /**
     * 变更通知单下发
     */
    @ApiOperation(value = "变更通知单下发功能", notes = "变更通知单")
    @PostMapping("/ENGG0005")
    public Result changeNoticeDistribute(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        JSONArray productionLineNoArray = object.getJSONObject("body").getJSONArray("productionLineNoArray");
        List<String> list = new ArrayList<>();
        //判断是否为空
        if (productionLineNoArray != null && productionLineNoArray.size() > 0) {
            for (int i = 0; i < productionLineNoArray.size(); i++) {
                //判断是否为机加供应商江苏高精的产线编号
                if ("JGXC1".equals(productionLineNoArray.get(i))) {
                    list.add(jcgjIp);
                }
                String productionLineIp = UrlUtils.URLMAP.get((String) productionLineNoArray.get(i));
                if (StringUtils.isNotEmpty(productionLineIp)) {
                    list.add(productionLineIp);
                }
            }
            if (list.size() > 0) {
                //ForwardUtils.postAsyncZK("http://172.30.200.201:30000","api/iottozk/changeNotice",object);
                ForwardUtils.postAsyncToZKTecos(list, object, "changeNotice");
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号组下的所有ip，请检查产线编号是否有问题");
            }
        }
        return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号数组为空");
    }

    /**
     * 临时变更工艺下发
     */
    @ApiOperation(value = "临时变更工艺下发功能", notes = "临时变更工艺")
    @PostMapping("/ENGG0012")
    public Result temporaryChanges(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        JSONArray productionLineNoArray = object.getJSONObject("body").getJSONArray("productionLineNoArray");
        List<String> list = new ArrayList<>();
        if (productionLineNoArray != null && productionLineNoArray.size() > 0) {
            for (int i = 0; i < productionLineNoArray.size(); i++) {
                if ("JGXC1".equals(productionLineNoArray.get(i))) {
                    list.add(jcgjIp);
                }
                String productionLineIp = UrlUtils.URLMAP.get((String) productionLineNoArray.get(i));
                if (StringUtils.isNotEmpty(productionLineIp)) {
                    list.add(productionLineIp);
                }
            }
            if (list.size() > 0) {
//                ForwardUtils.postAsyncZK("http://172.30.200.201:30000","api/iottozk/temporaryChangeNotice",object);
                ForwardUtils.postAsyncToZKTecos(list, object, "temporaryChangeNotice");
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号组下的所有ip，请检查产线编号是否有问题");
            }
        }
        return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号数组为空");
    }

    /**
     * ANDON主数据下发
     */
    @ApiOperation(value = "ANDON主数据下发功能", notes = "ANDON主数据")
    @PostMapping("/MFGG0001")
    public Result andonMasterData(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //由于ANDON主数据是全量发，所以代码如下
        Collection<String> values = UrlUtils.URLMAP.values();
        List<String> list = new ArrayList<>();
        list.add(jcgjIp);
        for (Object v : values) {
            list.add(v.toString());
        }
        list = list.stream().distinct().collect(Collectors.toList());
        if (list.size() > 0) {
//            ForwardUtils.postAsyncZK("http://172.30.200.201:30000","api/iottozk/admaster",object);
            ForwardUtils.postAsyncToZKTecos(list, object, "admaster");
            return Result.success(version, taskId, "ok");
        } else {
            return Result.error(version, ResponseEnum.ERROR, taskId, "数据表未找到产线编号以及其ip");
        }
    }

    /**
     * ANDON异常呼叫状态更新
     */
    @ApiOperation(value = "ANDON异常呼叫状态更新功能", notes = "ANDON异常呼叫状态更新")
    @PostMapping("/MFGG0006")
    public Result andonStatusUpate(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        String productionLineNo = object.getJSONObject("body").getString("productionLineNo");
        //防止空指针异常
        if (StringUtils.isNotEmpty(productionLineNo)) {
            ArrayList<String> list = new ArrayList<>();
            //判断是否为江苏高精和焊接线共用的产线编号
            if (productionLineNo.equals("JGXC1")) {
                //添加到list
                list.add(jcgjIp);
            }
            String ip = UrlUtils.URLMAP.get(productionLineNo);
            if (StringUtils.isNotEmpty(ip)) {
                list.add(ip);
            }
            if (list.size() > 0) {
//                ForwardUtils.postAsyncToZKTecos(list, object, "adCallUpdateStatus");
                ForwardUtils.postAsyncToZKTecos(list, object, "abnormalWarningRenew");
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
            }
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号为空");
        }
    }

    /**
     * 检验基准数据下发
     */
    @ApiOperation(value = "检验基准数据下发功能", notes = "检验基准数据")
    @PostMapping("/QMSG0001")
    public Result inspectionBenchmark(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //由于检验基准数据是全量发，所以代码如下
        Collection<String> values = UrlUtils.URLMAP.values();
        List<String> list = new ArrayList<>();
        for (Object v : values) {
            list.add(v.toString());
        }
        list.add(jcgjIp);
        list = list.stream().collect(Collectors.toList());
//      ForwardUtils.postAsyncZK("http://10.33.68.120:8085","test",object);
        ForwardUtils.postAsyncToZKTecos(list, object, "inspectionBasis");
        return Result.success(version, taskId, "ok");
    }

    /**
     * 生产日历下发
     */
    @ApiOperation(value = "生产日历下发功能", notes = "生产日历")
    @PostMapping("/MFGG0002")
    public Result productionCalendar(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //由于生产日历下发全量发，所以代码如下,数据表必须存在
        Collection<String> values = UrlUtils.URLMAP.values();
        List<String> list = new ArrayList<>();
        for (Object v : values) {
            list.add(v.toString());
        }
        list.add(jcgjIp);
        list = list.stream().collect(Collectors.toList());
//      ForwardUtils.postAsyncZK("http://172.30.200.201:30000","api/iottozk/productCalendar",object);
        ForwardUtils.postAsyncToZKTecos(list, object, "productCalendar");
        return Result.success(version, taskId, "ok");

    }

    /**
     * 工艺文件下发
     */
    @ApiOperation(value = "工艺文件下发功能", notes = "工艺文件")
    @PostMapping("/ENGG0004")
    public Result processDocument(@MyLog @RequestBody JSONObject object) {
        String version = object.getJSONObject("header").getString("version");
        String taskId = object.getJSONObject("header").getString("taskId");
        JSONArray productionLineNoArray = object.getJSONObject("body").getJSONArray("productionLineNoArray");
        List<String> list = new ArrayList<>();
        if (productionLineNoArray != null && productionLineNoArray.size() > 0) {
            for (int i = 0; i < productionLineNoArray.size(); i++) {
                String productionLineIp = UrlUtils.URLMAP.get((String) productionLineNoArray.get(i));
                if (StringUtils.isNotEmpty(productionLineIp)) {
                    list.add(productionLineIp);
                }
            }
            list = list.stream().distinct().collect(Collectors.toList());
            if (list.size() > 0) {
                //                ForwardUtils.postAsyncZK("http://172.16.25.3:9200/iot/api/data","processFile",object);
                ForwardUtils.postAsyncToZKTecos(list, object, "processFile");
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号组下的所有ip，请检查产线编号是否有问题");
            }
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号数组为空");
        }
    }

    /**
     * 生产工单下发
     */
    @Autowired
    private MomDownService momDownService;

    @ApiOperation(value = "生产工单下发功能", notes = "生产工单")
    @PostMapping("/MFGG0003")
    public Result productionOrder(@MyLog @RequestBody JSONObject object) throws InterruptedException {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        JSONArray productionLineNoArray = object.getJSONObject("body").getJSONArray("productionLineNoArray");
        // 将JSONArray转换为ArrayList<String>
        List<String> productionLineArrayList = new ArrayList<>();
        for (int i = 0; i < productionLineNoArray.size(); i++) {
            productionLineArrayList.add(productionLineNoArray.getString(i));
        }
        List<String> productionLineNameList = urlService.getProductionLineNameByNo(productionLineArrayList);

        HashMap<String, String> map = new HashMap<String, String>(productionLineNameList.size());
        String productionLineNameStr = "";
        for (int i = 0; i < productionLineNameList.size(); i++) {
            if (map.containsKey(productionLineNameList.get(i))) {
            } else {
                map.put(productionLineNameList.get(i), productionLineNameList.get(i));
                productionLineNameStr += productionLineNameList.get(i) + ",";
            }
        }
        //定义产线ip数组
        List<String> productIps = new ArrayList<>();
        if (productionLineNoArray != null && productionLineNoArray.size() > 0) {
            for (int i = 0; i < productionLineNoArray.size(); i++) {
                String productionLineIp = UrlUtils.URLMAP.get((String) productionLineNoArray.get(i));
                //如果是中机中联的产线编号，则单独处理
                for (int j = 0; j < UrlUtils.LINECODELIST.size(); j++) {
                    if (UrlUtils.LINECODELIST.get(j).equals(productionLineNoArray.get(i))) {
                        IotQueueUtil.addToQueue(object);
                        String lineCode = UrlUtils.LINECODELIST.get(j);
                        taskExecutor.execute(()->{
                            try {
                                momDownService.momDownZJZL(lineCode);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
                if (productionLineNoArray.get(i).equals("JGXC1")) { //机加江苏高精的产线编号
                    productIps.add(jcgjIp);
                } else if (productionLineIp != null) {
                    productIps.add(productionLineIp);
                }
            }
            productIps = productIps.stream().distinct().collect(Collectors.toList());
            if (productIps.size() > 0) {
//                ForwardUtils.postAsyncToZKTecos(productIps, object, "productOrder");
                ForwardUtils.postAsyncToZKTecos(productIps, object, "productOrder", productionLineNameStr);
            }
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号数组为空");
        }
        return Result.success(version, taskId, "ok");
    }

    /**
     * 配盘工单下发
     */
    @ApiOperation(value = "配盘工单下发功能", notes = "配盘工单")
    @PostMapping("/LESG0006")
    public Result compositionTechniqueOrder(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //IOT只发给下料中控，所以写死ip
        // String productionLineIp = UrlUtils.URLMAP.get("xl-0001");
//        ForwardUtils.postAsyncZK("http://172.16.25.3:9200/iot/api/data","pairPlateOrder",object);
        ForwardUtils.postAsyncZK(materialsId, "pairPlateOrder", object);

        return Result.success(version, taskId, "ok");
    }

    /**
     * 供应商主数据下发
     */
    @ApiOperation(value = "供应商主数据下发功能", notes = "供应商主数据")
    @PostMapping("/QMSG0003")
    public Result supplierMasterData(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //IOT只发给下料中控，所以写死ip
        //String ip="127.0.0.1";
        ForwardUtils.postAsyncZK(materialsId, "supplierBasis", object);
        return Result.success(version, taskId, "ok");
    }

    /**
     * 缺陷代码下发
     */
    @ApiOperation(value = "缺陷代码下发功能", notes = "缺陷代码")
    @PostMapping("/QMSG0002")
    public Result defectiveCode(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        //IOT只发给下料中控，所以写死ip
        //String ip="127.0.0.1";
        ForwardUtils.postAsyncZK(materialsId, "defectCodeBasis", object);
        return Result.success(version, taskId, "ok");
    }

    /**
     * 空容器配送指令下发
     */
    @ApiOperation(value = "空容器配送指令下发功能", notes = "空容器配送指令")
    @PostMapping("/LESG0011")
    public Result emptyContainerCommand(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        String productionLineNo = object.getJSONObject("body").getString("productionLineNo");
        if (StringUtils.isNotEmpty(productionLineNo)) {
            String ip = UrlUtils.URLMAP.get(productionLineNo);
            if (StringUtils.isNotEmpty(ip)) {
//                ForwardUtils.postAsyncZK("http://10.33.68.120:8085","test",object);
                ForwardUtils.postAsyncZK(ip, "emptyContainerCommand", object);
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
            }

        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号为空");
        }
    }

    /**
     * 配盘结果下发
     */
    @ApiOperation(value = "配盘结果下发功能", notes = "配盘结果")
    @PostMapping("/LESG0008")
    public Result allocationResult(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        String productionLineNo = object.getJSONObject("body").getString("productionLineNo");
        if (StringUtils.isNotEmpty(productionLineNo)) {
            String ip = UrlUtils.URLMAP.get(productionLineNo);
            if (StringUtils.isNotEmpty(ip)) {
//                ForwardUtils.postAsyncZK("http://10.33.68.120:8085","test",object);
                ForwardUtils.postAsyncZK(ip, "pairPlateResult", object);
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
            }
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号为空");
        }
    }

    /**
     * 配送反馈下发
     */
    @ApiOperation(value = "配送反馈下发功能", notes = "配送反馈")
    @PostMapping("/LESR0002")
    public Result deliveryFeedback(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        String productionLineNo = object.getJSONObject("body").getString("productionLineNo");
        if (StringUtils.isNotEmpty(productionLineNo)) {
            ArrayList<String> ips = new ArrayList<>();
            //如果是此产线编号，则考虑给江苏高精
            if (productionLineNo.equals("JGXC1")) {
                ips.add(jcgjIp);
            }
            //其他正常获取写入
            String ip = UrlUtils.URLMAP.get(productionLineNo);
            if (StringUtils.isNotEmpty(ip)) {
                ips.add(ip);
//                ForwardUtils.postAsyncZK("http://172.16.25.3:9200/iot/api/data","deliveryStatus",object);
            }
            if (ips.size() > 0) {
                ForwardUtils.postAsyncToZKTecos(ips, object, "deliveryStatus");
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
            }


        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号为空");
        }

    }

    /**
     * 配送任务触发
     */
    @ApiOperation(value = "配送任务触发功能", notes = "配送任务触发")
    @PostMapping("/LESG0009")
    public Result deliveryTask(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        String productionLineNo = object.getJSONObject("body").getString("productionLineNo");
        if (StringUtils.isNotEmpty(productionLineNo)) {
            String ip = UrlUtils.URLMAP.get(productionLineNo);
            if (StringUtils.isNotEmpty(ip)) {
//                ForwardUtils.postAsyncZK("http://172.16.25.3:9200/iot/api/data","deliveryTask",object);
//                ForwardUtils.postAsyncZK(ip,"deliveryTask",object);
                ForwardUtils.postAsyncZK(ip, "callTaskIssue", object);
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
            }

        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号为空");
        }

    }

    /**
     * 专检结果下发
     */
    @ApiOperation(value = "专检结果下发功能", notes = "专检结果")
    @PostMapping("/QMSG0004")
    public Result specialInspectionResult(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        String productionLineNo = object.getJSONObject("body").getString("productionLineNo");
        if (StringUtils.isNotEmpty(productionLineNo)) {
            String ip = UrlUtils.URLMAP.get(productionLineNo);
            if (StringUtils.isNotEmpty(ip)) {
//                ForwardUtils.postAsyncZK("http://172.16.25.3:9200/iot/api/data","specialInspectionResult",object);
                ForwardUtils.postAsyncZK(ip, "specialInspectionResult", object);
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
            }

        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号为空");
        }
    }

    /**
     * 打印功能
     *
     * @param object
     * @return
     * @throws Exception 异常
     *                   如果打印失败
     */
    @ApiOperation(value = "打印功能", notes = "打印功能")
    @PostMapping("/MFGG0007")
    public Result print(@MyLog @RequestBody JSONObject object) {

        Object execute = Forest.post(printserverIp).setMaxRetryInterval(120000L).addBody(object).setBodyType(ForestDataType.JSON).setContentType("application/json;charset=utf-8").onError((ex, req, res) -> {

        }).onSuccess((ex, req, res) -> {
        }).execute();
        JSONObject o = (JSONObject) JSON.parse(execute.toString());
        Integer code = Integer.parseInt(o.get("code").toString());
        if (code == 0) {
            return Result.success(o.get("version").toString(), o.get("taskId").toString(), o.get("returnData"));
        }
        return Result.error(o.get("version").toString(), ResponseEnum.NOTFOUND, o.get("taskId").toString(), o.get("returnData"));
    }

//    /**
//     * 打印功能
//     * @param object
//     * @return
//     * @throws Exception 异常
//     * 如果打印失败
//     */
     /*
    @ApiOperation(value = "打印功能",notes = "打印功能")
    @PostMapping("/MFGG0007")
    public Result print(@MyLog @RequestBody JSONObject object){
        JSONObject body = object.getJSONObject("body");
        String version=object.getJSONObject("header").getString("version");
        String taskId=object.getJSONObject("header").getString("taskId");
        String fileUrl = body.getString("fileAddress");

        String workStation=body.getString("workStation");
        if (StringUtils.isNotEmpty(fileUrl)&&StringUtils.isNotEmpty(workStation)){
            PrintFile printFile = new PrintFile();
            printFile.setUuid(UUIDUtil.generate());
            Date date = new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            printFile.setCreateTime(sdf.format(date));
            printFile.setFileUrl(fileUrl);
            printFile.setTaskId(taskId);
            printFile.setWorkStation(workStation);
            //异步插入
            this.insert(printFile);
            //调用打印机进行打印
            PrintUtil printUtil = new PrintUtil();
            try {
                String printName=printUtil.PRINTMAP.get(workStation);
                PrintUtil.print(fileUrl,printName, PrintParameter.getPrintRequestAttributeSet(),PrintParameter.builder(), PaperType.DEFAULT);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return Result.success(version,Result.getTaskId(object),"打印成功");
        }else {
            return Result.error(version,ResponseEnum.PARAMERROR,"","文件链接为空或工位编号为空");

        }

    }
    @Async
    public void insert(PrintFile printFile){
        printFileService.insertPrintFile(printFile);
    }*/

    /**
     * 工件绑定订单回传中控
     */
    @ApiOperation(value = "工件绑定订单回传中控", notes = "工件绑定订单回传中控")
    @PostMapping("/MFGG0008")
    public Result workpieceBindOrder(@MyLog @RequestBody JSONObject object) throws InterruptedException {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        String productionLineNo = object.getJSONObject("body").getString("productionLineNo");
        if (StringUtils.isNotEmpty(productionLineNo)) {
            //判断是否为中机中联的产线编号
            for (int i = 0; i < UrlUtils.LINECODELIST.size(); i++) {
                if (UrlUtils.LINECODELIST.get(i).equals(productionLineNo)) {
                    IotQueueUtil.addToQueue2(object);
                    momDownService.momDownBinding(productionLineNo);
                    return Result.success(version, taskId, "ok");
                }
            }
//           else{
            String ip = UrlUtils.URLMAP.get(productionLineNo);
            //是否为空
            if (StringUtils.isNotEmpty(ip)) {
//                ForwardUtils.postAsyncZK("http://172.30.200.201:30000","api/iottozk/workpieceBindOrder",object);
                ForwardUtils.postAsyncZK(ip, "workpieceBindOrder", object);
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
            }
//            }
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号为空");
        }
    }

    /**
     * SRM下发送货单(下料中控)
     */
    @ApiOperation(value = "SRM下发送货单(下料中控)", notes = "SRM下发送货单(下料中控)")
    @PostMapping("/LESR(G)0014")
    public Result deliveryNote(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        if (StringUtils.isNotEmpty(materialsId)) {
//                ForwardUtils.postAsyncZK(materialsId,"test",object);
//            ForwardUtils.postAsyncZK(materialsId,"deliveryNote",object);
            ForwardUtils.postAsyncZK(materialsId, "srmDeliveryIssue", object);
            return Result.success(version, taskId, "ok");
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
        }
    }


    /**
     * SRM下发采购订单(下料中控)
     */
    @ApiOperation(value = "SRM下发采购订单(下料中控)", notes = "SRM下发采购订单(下料中控)")
    @PostMapping("/LESR(G)0016")
    public Result procureOrder(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        if (StringUtils.isNotEmpty(materialsId)) {
//            ForwardUtils.postAsyncZK(materialsId,"test",object);
//            ForwardUtils.postAsyncZK(materialsId,"procureOrder",object);
            ForwardUtils.postAsyncZK(materialsId, "srmPurchaseIssue", object);
            return Result.success(version, taskId, "ok");
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
        }
    }

    /**
     * 钢板库存查询(下料中控)
     */
    @ApiOperation(value = "钢板库存查询(下料中控)", notes = "钢板库存查询(下料中控)")
    @PostMapping("/LESR0019")
    public JSONObject spInventory(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        if (StringUtils.isNotEmpty(materialsId)) {
            JSONObject jsonObject = ForwardUtils.postAwaitResultAndUpload(materialsId, "spInventory", object);
//            JSONObject jsonObject = ForwardUtils.postAwaitResult(materialsId, "test2", object);
            return jsonObject;
        } else {
            return (JSONObject) JSONObject.toJSON(Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到下料中控IP"));
        }
    }


    /**
     * 零件库存查询(下料中控）
     */
    @ApiOperation(value = "零件库存查询(下料中控）", notes = "零件库存查询(下料中控）")
    @PostMapping("/LESR0020")
    public JSONObject partsInventory(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        if (StringUtils.isNotEmpty(materialsId)) {
            JSONObject jsonObject = ForwardUtils.postAwaitResultAndUpload(materialsId, "partsInventory", object);
            return jsonObject;
        } else {
            return (JSONObject) JSONObject.toJSON(Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到下料中控IP"));
        }
    }

    /**
     * MOM下发下料审批信息(下料中控)
     */
    @ApiOperation(value = "MOM下发下料审批信息(下料中控)", notes = "MOM下发下料审批信息(下料中控)")
    @PostMapping("/QMSG0005")
    public Result examine(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        if (StringUtils.isNotEmpty(materialsId)) {
//            ForwardUtils.postAsyncZK(materialsId,"test",object);
            ForwardUtils.postAsyncZK(materialsId, "examine", object);
            return Result.success(version, taskId, "ok");
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
        }
    }


    /**
     * 改派下发
     */
    @ApiOperation(value = "改派下发", notes = "改派下发")
    @PostMapping("/MFGG0010")
    public Result reassignment(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        String productionLineNo = object.getJSONObject("body").getString("productionLineNo");
        if (StringUtils.isNotEmpty(productionLineNo)) {
            String ip = UrlUtils.URLMAP.get(productionLineNo);
            if (StringUtils.isNotEmpty(ip)) {
//                ForwardUtils.postAsyncZK("http://172.16.25.3:9200/iot/api/data","reassignment",object);
                ForwardUtils.postAsyncZK(ip, "reassignment", object);
                return Result.success(version, taskId, "ok");
            } else {
                return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
            }

        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号为空");
        }
    }

    /**
     * NC程序下发
     * 下发到机加中控
     */
    @ApiOperation(value = "NC程序下发", notes = "NC程序下发")
    @PostMapping("/ENGG0007")
    public Result ncCodeIssue(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");


        //下发给机加中控
        ForwardUtils.postAsyncZK(jcgjIp, "ncProcedure", object);

        return Result.success(version, taskId, "ok");

    }

    /**
     * 涂装自制件出库队列下发下发
     * 下发到涂装中控
     */
    @ApiOperation(value = "涂装自制件出库队列下发下发", notes = "涂装自制件出库队列下发下发")
    @PostMapping("/LESG0013")
    public Result bqnIssue(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        JSONArray productionLineNoArray = object.getJSONObject("body").getJSONArray("productionLineNoArray");

        List<String> productList = new ArrayList<>();
        if (productionLineNoArray != null && productionLineNoArray.size() > 0) {

            for (int i = 0; i < productionLineNoArray.size(); i++) {
                String productionLineIp = UrlUtils.URLMAP.get((String) productionLineNoArray.get(i));
                productList.add(productionLineIp);
//               if (StringUtils.isEmpty(productionLineIp)){
//                    return Result.error(version,ResponseEnum.PARAMERROR,taskId,"找不到此产线编号组下的所有ip，请检查产线编号是否有问题");
//                }
            }
            List<String> products = productList.stream().distinct().collect(Collectors.toList());

            for (String productionLineIp : products) {
                ForwardUtils.postAsyncZK(productionLineIp, "outboundQueue", object);
            }

        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "产线编号数组为空");
        }

        return Result.success(version, taskId, "ok");

    }

    /**
     * 预套料数据下发(下料中控)
     */
    @ApiOperation(value = "预套料数据下发(下料中控)", notes = "预套料数据下发(下料中控)")
    @PostMapping("/MFGG0012")
    public Result beforehandJacking(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        if (StringUtils.isNotEmpty(materialsId)) {
//            ForwardUtils.postAsyncZK(materialsId,"test",object);
            ForwardUtils.postAsyncZK(materialsId, "preNestDataIssue", object);
            return Result.success(version, taskId, "ok");
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
        }
    }

    /**
     * 实际套料数据下发(下料中控)
     */
    @ApiOperation(value = "实际套料数据下发(下料中控)", notes = "实际套料数据下发(下料中控)")
    @PostMapping("/MFGG0013")
    public Result realityJacking(@MyLog @RequestBody JSONObject object) {
        String taskId = object.getJSONObject("header").getString("taskId");
        String version = object.getJSONObject("header").getString("version");
        if (StringUtils.isNotEmpty(materialsId)) {
//            ForwardUtils.postAsyncZK(materialsId,"test",object);
//            ForwardUtils.postAsyncZK(materialsId,"realityExamine",object);
            ForwardUtils.postAsyncZK(materialsId, "actualNestDataIssue", object);
            return Result.success(version, taskId, "ok");
        } else {
            return Result.error(version, ResponseEnum.PARAMERROR, taskId, "找不到此产线编号对应的ip，请检查产线编号是否有问题");
        }
    }


}