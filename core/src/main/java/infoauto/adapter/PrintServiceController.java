package infoauto.adapter;


import infoauto.entity.PrintParameterConfig;
import infoauto.entity.PrintService;
import infoauto.service.clutter.PrintParameterConfigService;
import infoauto.service.clutter.PrintServiceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by InfoAuto.
 * 此Controller针对打印服务相关的处理,自动打印请查看MOMController
 * @author : zhuangweizhong
 * @create 2023/6/19 14:48
 */
@Controller
@RequestMapping("printService")
@Api("打印服务接口")
public class PrintServiceController {

    @Autowired
    private PrintServiceService printService;

    @Autowired
    private  PrintParameterConfigService parameterConfigService;

    /**
     * 获取所有可用的打印服务
     * @return
     */
    @GetMapping("/getPrintService")
    @ResponseBody
    public List<PrintService> getPrintService(){
        return printService.getPrintService();
    }

//    @GetMapping("/getPrintParameter")
//    @ResponseBody
//    public PrintParameterConfig getPrintParameter(){
//        return parameterConfigService.selectPrintParameterConfigForWorkStation("0001");
//    }
}
