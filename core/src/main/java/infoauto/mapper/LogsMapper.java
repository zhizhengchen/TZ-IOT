package infoauto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import infoauto.entity.Logs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2024/1/29 16:27
 */
@Mapper
public interface LogsMapper  extends BaseMapper<Logs> {
    //查询分页
    @Select("SELECT taskId,version,taskType,time,data FROM business_log order by time desc limit #{page},#{limit}")
    List<Logs> selectLogsLimitDesc(@Param("page") Integer page, @Param("limit") Integer limit);
    @Select("SELECT taskId,version,taskType,time,data FROM business_log order by time asc limit #{page},#{limit}")
    List<Logs> selectLogsLimitAsc(@Param("page") Integer page, @Param("limit") Integer limit);
    @Select("SELECT COUNT(*) FROM business_log")
    Integer selectLogsCount();
}
