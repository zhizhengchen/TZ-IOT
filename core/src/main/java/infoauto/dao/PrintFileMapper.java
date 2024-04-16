package infoauto.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import infoauto.entity.PrintFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/15 15:01
 */
@Mapper
public interface PrintFileMapper extends BaseMapper<PrintFile> {
    //查询分页所有内容
    @Select("SELECT uuid,file_url,create_time,work_station,task_id FROM print_file order by create_time desc limit #{page},#{limit}")
    List<PrintFile> selectList(@Param("page") Integer page, @Param("limit") Integer limit);
    //统计条数
    @Select("SELECT count(uuid) FROM print_file")
    Integer getPrintFileCount();
    //根据taskId查询printFile
    @Select("SELECT * FROM print_file WHERE task_id=#{taskId}")
    PrintFile getPrintFileForTaskId(@Param("taskId") String taskId);
    //根据工位编号查找printFile集合
    @Select("SELECT * FROM print_file WHERE work_station=#{workStation}")
    List<PrintFile> selectPrintFileForWorkStation(@Param("workStation") String workStation);
}
