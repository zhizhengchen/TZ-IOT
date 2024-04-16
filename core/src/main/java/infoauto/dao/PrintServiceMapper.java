package infoauto.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import infoauto.entity.PrintService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/15 13:31
 */
@Repository
@Mapper
public interface PrintServiceMapper extends BaseMapper<PrintService> {
}
