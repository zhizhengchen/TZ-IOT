package infoauto.service.clutter;

import infoauto.entity.PrintParameterConfig;
import org.apache.ibatis.annotations.Param;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/8/22 11:40
 */
public interface PrintParameterConfigService {

    PrintParameterConfig selectPrintParameterConfigForWorkStation(String workStation);
}
