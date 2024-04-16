package infoauto.service.clutter;

import infoauto.entity.Logs;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2024/1/29 16:21
 */
public interface LogsService {

    //根据正序倒序并选择页数进行查询
    List<Logs> getLogsLimit(String direction,Integer page);

    Integer getPageMax();
}
