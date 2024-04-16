package infoauto.adapter;

import com.alibaba.fastjson.JSONObject;

import infoauto.entity.PrintFile;
import infoauto.enums.PaperType;
import infoauto.enums.ResponseEnum;
import infoauto.service.clutter.PrintFileService;
import infoauto.service.clutter.PrintServiceService;
import infoauto.util.PageUtil;
import infoauto.util.PrintParameter;
import infoauto.util.PrintUtil;
import infoauto.util.Result;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by InfoAuto.
 * 此Controller针对打印相关的功能
 * @author : zhuangweizhong
 * @create 2023/6/15 13:58
 */

@Controller
@RequestMapping("print")
@Api("打印功能接口")
public class PrintController {
    //打印服务的Service
    @Autowired
    PrintServiceService printService;

    @Autowired
    PrintFileService printFileService;

//    @Value("${fUrl}")
    private String fUrl;

    //打印服务页面跳转
    @RequestMapping("/printPage")
    public String printPage(){
        return "printPage.html";
    }

    //选择指定的文件链接以及指定的打印服务进行打印
    @ResponseBody
    @PostMapping("/printFileUrl")
    public Result printForFileUrl(@RequestBody JSONObject jsonObject){

        //1、获取值
        //文件链接地址

        //String fileUrl="\\\\XIAOZHUANG\\uploadFile\\";
        String fileUrl = fUrl + jsonObject.getJSONObject("body").getString("fileAddress");
        //任务号"
        String taskId = jsonObject.getJSONObject("header").getString("taskId");
        //工位编号
        String workStation = jsonObject.getJSONObject("body").getString("workStation");
        //打印类型，只可选A4和自定义类型
        String type = jsonObject.getString("type");
        //2、自定义值
        //UUID
        String uuid= UUID.randomUUID().toString().replaceAll("-", "");

        //创建时间
        Date date = new Date();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(date);

        //设置值
        PrintFile printFile = new PrintFile();
        printFile.setFileUrl(fileUrl);
        printFile.setUuid(uuid);
        printFile.setTaskId(taskId);
        printFile.setWorkStation(workStation);
        printFile.setCreateTime(createTime);
        //添加需要打印文件路径
        printFileService.insertPrintFile(printFile);


        //根据工位编号获取打印服务名称
        String printerName = PrintUtil.PRINTMAP.get(workStation);

        //若打印服务找不到，则需要根据选项选择打印服务名称
        if(StringUtils.isEmpty(printerName)){
            return Result.error("",ResponseEnum.NOTFOUND,taskId,"打印失败，找不到工位编号："+workStation+"下的打印机");

        }else {
            //打印
            try {
                PrintUtil.print(fileUrl,printerName, PrintParameter.getPrintRequestAttributeSet(), PrintParameter.builder(), PaperType.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //返回参数
            return Result.success("",taskId,"打印成功");
        }

    }

    /**
     * 查询所有的打印文件
     * @return
     */
    @GetMapping("/getPrintFileAll")
    @ResponseBody
    public PageUtil getPrintFileAll(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit){

        //查询总条数
       Integer count= printFileService.getPrintFileCount();
        //查询结果
        List<PrintFile> printFileAll = printFileService.getPrintFileAll(page,limit);
        //返回给前端
        return PageUtil.success(page,count,printFileAll);
    }

    /**
     * 手动指定打印服务和文件进行打印
     * @throws Exception
     */

    @PostMapping("/printForFile")
    @ResponseBody
    public Result printForFile(@RequestBody JSONObject object) {
        String workStation = object.get("workStation").toString();
        String taskId=object.get("taskId").toString();
        try {
            String[] s = new String[1];
            s[0]= workStation;
            String printerName = PrintUtil.PRINTMAP.get(workStation);
            if(StringUtils.isEmpty(printerName)){
                return Result.error("",ResponseEnum.NOTFOUND,taskId,"打印失败，找不到工位编号："+workStation+"下的打印机");

            }else {
                //打印
                try {
                    PrintUtil.print(new File(object.get("filePath").toString()),printerName,PrintParameter.getPrintRequestAttributeSet(),PrintParameter.builder(),PaperType.DEFAULT,s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //返回参数
                return Result.success("",taskId,"1");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success("",taskId,"1");
    }

    /**
     * 根据任务ID查找指定的内容
     * @return
     */
    @GetMapping("/getPrintFileForTaskID")
    @ResponseBody
    public Object getPrintFileForTaskID(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,@RequestParam("taskId")String taskId){
        List<PrintFile> list=new ArrayList<>();
        if (StringUtils.isNotEmpty(taskId)){
            PrintFile printFile=printFileService.getPrintFileForTaskId(taskId);
            list.add(printFile);
            return Result.success("","",list);
        }else {
            //查询总条数
            Integer count= printFileService.getPrintFileCount();
            list= printFileService.getPrintFileAll(page,limit);
            return PageUtil.success(page,count,list);
        }
    }

    @GetMapping("/getPrintFileForWorkStation")
    @ResponseBody
    public Object getPrintFileForWorkStation(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,@RequestParam("workStation")String workStation){
        List<PrintFile> list=new ArrayList<>();
        if (StringUtils.isNotEmpty(workStation)){
            list=printFileService.getPrintFileForWorkStation(workStation);
            return Result.success("","",list);
        }else {
            //查询总条数
            Integer count= printFileService.getPrintFileCount();
            list=printFileService.getPrintFileAll(page,limit);
            return PageUtil.success(page,count,list);
        }
    }
}
