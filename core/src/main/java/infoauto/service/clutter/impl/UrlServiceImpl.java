package infoauto.service.clutter.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import infoauto.dao.UrlMapper;
import infoauto.entity.ProductionLineUrl;
import infoauto.service.clutter.UrlService;
import infoauto.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/11 22:56
 */
@Service
@Slf4j
public class UrlServiceImpl implements UrlService {

    @Autowired
    UrlMapper urlMapper;
    //是否启用
//    @Value("${supplier.enable}")
//    private  String enable;
    /**
     * 查询所有的产线编号与Ip
     */
    @Override
    public void setProductNoAndUrlForUrlUtils() {
        QueryWrapper<ProductionLineUrl> wrapper = new QueryWrapper<>();
        wrapper.eq("state",1);
        List<ProductionLineUrl> urlList = urlMapper.selectList(wrapper);

        System.out.println(urlList);
        if(!urlList.isEmpty()){
            urlList.forEach(urlUtils -> {
                UrlUtils.URLMAP.put(urlUtils.getProductionLineNo(),urlUtils.getUrl());
            });
        }else {
            log.info("数据表production_line_url的内容为空，请添加数据，否则影响后续功能的使用");
        }
    }

    /**
     * 中机中联产线编号List
     */
    @Override
    public void setProductNoAndZJZLForUrlUtils() {
        UrlUtils.LINECODELIST.add("ZZ210");
        UrlUtils.LINECODELIST.add("ZZ220");
        UrlUtils.LINECODEMAP.put("ZZ210","gd-zpgdxf-10.73.149.2");
        UrlUtils.LINECODEMAP.put("ZZ220","gd-zpgdxf-10.73.148.2");

//        QueryWrapper<ProductionLineUrl> wrapper = new QueryWrapper<>();
//        wrapper.eq("state",2);
//        List<ProductionLineUrl> urlList = urlMapper.selectList(wrapper);
//
//        System.out.println(urlList);
//        if(!urlList.isEmpty()){
//            urlList.forEach(urlUtils -> {
//                UrlUtils.LINECODEMAP.put(urlUtils.getProductionLineNo(),urlUtils.getUrl());
//            });
//        }else {
//            log.info("数据表production_line_url的中机中联产线编号内容为空，请添加数据，否则影响后续功能的使用");
//        }
    }
//
//    /**
//     * 根据供应商名称批量更新是否启用
//     * @param state
//     * @param suppliers
//     * @return
//     */
//    @Override
//    public int updateStateForSupplier(int state,List<String> suppliers) {
//        /**
//         * 根据供应商修改是否给该供应商下发功能，仅用于联调测试！
//         * @param supplier
//         */
//            //认为该功能启用
//            if (enable.equals("true")){
//                return urlMapper.updateStateForSupplier(state,suppliers);
//            }else{
//                return -1;
//            }
//    }

    @Override
    public List<String> getProductionLineNameByNo(List<String> productionLineArrayList) {
        return urlMapper.getProductionLineNameByNo(productionLineArrayList);
    }
}
