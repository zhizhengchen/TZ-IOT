package infoauto.service.clutter;



import infoauto.entity.PrintService;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/15 13:32
 */
public interface PrintServiceService {
    void setPrintServiceNameAndWorkStation();

    List<PrintService> getPrintService();

}
