package infoauto.service.clutter.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import infoauto.dao.TestMapper;
import infoauto.entity.ProductionLineUrl;
import infoauto.service.clutter.ProductionLineUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductionLineUrlServiceImpl extends ServiceImpl<TestMapper, ProductionLineUrl> implements ProductionLineUrlService {

    @Autowired
    private TestMapper testMapper;
    @Autowired
    private UrlServiceImpl urlService;



    @Override
    public List<ProductionLineUrl> select() {
        List<ProductionLineUrl> list = testMapper.selectList(null);
        return list;
    }

    @Override
    public int add(ProductionLineUrl plu) {
        plu.setState(1);
        int i = testMapper.insert(plu);
        //更新Map
        urlService.setProductNoAndUrlForUrlUtils();
        return i;
    }

    @Override
    public int updateTab(ProductionLineUrl plu) {
        UpdateWrapper<ProductionLineUrl> wrapper = new UpdateWrapper<>();
        wrapper.eq("production_line_no",plu.getProductionLineNo());
        int i = testMapper.update(plu, wrapper);
        //更新Map
        urlService.setProductNoAndUrlForUrlUtils();
        return i;
    }

    @Override
    public int delete(String productionLineNo) {
        UpdateWrapper<ProductionLineUrl> wrapper = new UpdateWrapper<>();
        wrapper.eq("production_line_no",productionLineNo);
        wrapper.set("state",0);
        int i = testMapper.update(new ProductionLineUrl(),wrapper);
        //更新Map
        urlService.setProductNoAndUrlForUrlUtils();
        return i;
    }



    @Override
    public List<ProductionLineUrl> selectByProductionLine(String productionLine) {
        LambdaQueryWrapper<ProductionLineUrl> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductionLineUrl::getProductionLineNo,productionLine);
        List<ProductionLineUrl> productionLineUrls = testMapper.selectList(wrapper);
        return productionLineUrls;
    }
}
