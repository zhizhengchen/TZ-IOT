package infoauto.controller.clutter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2024/1/29 16:07
 */
@Controller
public class LogsPageController {
    //打印服务页面跳转
    @RequestMapping("/interface/logsShow")
    public String printPage(){
        return "logsPage";
    }
}
