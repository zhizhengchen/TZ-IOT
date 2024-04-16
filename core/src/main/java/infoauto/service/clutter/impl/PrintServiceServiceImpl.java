package infoauto.service.clutter.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import infoauto.dao.PrintServiceMapper;
import infoauto.entity.PrintService;
import infoauto.service.clutter.PrintServiceService;
import infoauto.util.PrintUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/15 13:34
 */
@Service
@Slf4j
public class PrintServiceServiceImpl implements PrintServiceService {
    @Autowired
    PrintServiceMapper mapper;

    @Override
    public void setPrintServiceNameAndWorkStation() {

        QueryWrapper<PrintService> wrapper = new QueryWrapper<>();
        List<PrintService> lists = mapper.selectList(wrapper);
        if(!lists.isEmpty()){
            lists.forEach(list -> {
                PrintUtil.PRINTMAP.put(list.getWorkStation(),list.getPrinterName());
            });
        }else {
            log.info("表print_service的数据为空，无法进行调用打印功能，请添加打印服务数据至表");
        }
    }

    @Override
    public List<PrintService> getPrintService() {
        QueryWrapper<PrintService> wrapper = new QueryWrapper<>();
        wrapper.eq("state",1);
        return mapper.selectList(wrapper);
    }
}
