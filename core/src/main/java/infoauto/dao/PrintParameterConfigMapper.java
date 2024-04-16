package infoauto.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import infoauto.entity.PrintParameterConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/8/22 11:38
 */
@Mapper
public interface PrintParameterConfigMapper extends BaseMapper<PrintParameterConfig> {
    //根据工位编号查找参数值
    @Select("SELECT * FROM print_parameter_config WHERE work_station=#{workStation}")
    PrintParameterConfig selectPrintParameterConfigForWorkStation(@Param("workStation") String workStation);
}
