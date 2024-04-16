package infoauto.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import infoauto.entity.ProductionLineUrl;
import infoauto.util.UrlUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/11 22:57
 */
@Repository
@Mapper
public interface UrlMapper extends BaseMapper<ProductionLineUrl> {
    //    //根据供应商批量修改状态
//    @Modifying
//    @Update("UPDATE production_line_url SET state=#{state} WHERE supplier in (#{suppliers})")
//    int updateStateForSupplier(int state,List<String> suppliers);
    @Select({
            "<script>" +
                    "SELECT production_line_name FROM production_line_url",
            "WHERE production_line_no IN <foreach collection='productionLineNoList' index='index' item='item' open='(' separator=',' close=')'>" +
                    "#{item}" +
                    "</foreach>" +
                    "</script>"
    })
    List<String> getProductionLineNameByNo(@Param("productionLineNoList") List<String> productionLineNoList);
}
