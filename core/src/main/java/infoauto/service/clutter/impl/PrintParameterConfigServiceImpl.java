package infoauto.service.clutter.impl;

import infoauto.dao.PrintParameterConfigMapper;
import infoauto.entity.PrintParameterConfig;
import infoauto.service.clutter.PrintParameterConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/8/22 11:41
 */
@Service
public class PrintParameterConfigServiceImpl implements PrintParameterConfigService {
    @Autowired
    PrintParameterConfigMapper printParameterConfigMapper;
    //根据工位编号查询打印参数
    @Override
    public PrintParameterConfig selectPrintParameterConfigForWorkStation(String workStation) {
        return printParameterConfigMapper.selectPrintParameterConfigForWorkStation(workStation);
    }
}
