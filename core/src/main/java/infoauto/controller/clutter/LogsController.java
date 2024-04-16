package infoauto.controller.clutter;

import infoauto.dto.LogsDTO;
import infoauto.entity.Logs;

import infoauto.enums.ResponseEnum;
import infoauto.service.clutter.LogsService;
import infoauto.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2024/1/29 16:13
 * @update czz 2024 0411 更改分页条件
 */
@Controller
@RequestMapping("logs")
public class LogsController {
   @Autowired
   private LogsService logsService;

    //根据方向和页数查询业务日志
    @GetMapping("/getLogsLimit")
    @ResponseBody
    public LogsDTO getLogsLimit(@RequestParam("direction") String direction, @RequestParam("page") Integer page){

        //固定每次仅可查询20条数据，可选择正序排序和倒叙排序，选择页数查询具体内容
        //direction 的值：desc   asc
        if (direction.isEmpty()|| page<=0){
            return new LogsDTO(page,0,direction,null, ResponseEnum.PARAMERROR.getCode());
        }else {
            List<Logs> logsList = logsService.getLogsLimit(direction, page * 20);
            //设置page,查询条件，最大页数
            Integer pageMax=(int) Math.ceil(logsService.getPageMax()/20);
            return new LogsDTO(page,pageMax,direction,logsList,ResponseEnum.SUCCESS.getCode());
        }
    }
}
