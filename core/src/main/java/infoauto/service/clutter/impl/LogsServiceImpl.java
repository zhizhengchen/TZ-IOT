package infoauto.service.clutter.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import infoauto.entity.Logs;
import infoauto.mapper.LogsMapper;
import infoauto.service.clutter.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2024/1/29 16:22
 */
@Service
public class LogsServiceImpl implements LogsService {

    @Autowired
    LogsMapper logsMapper;

    @Override
    public List<Logs> getLogsLimit(String direction, Integer page) {
        if (direction.equals("desc")){
            return logsMapper.selectLogsLimitDesc(page-1,20);

        }else{
            return logsMapper.selectLogsLimitAsc(page-1,20);
        }
    }

    @Override
    public Integer getPageMax() {
        //获取总数量
       Integer count= logsMapper.selectLogsCount();
        //计算得出总页数
        return count==0?1:(count/20)+1;
    }
}
