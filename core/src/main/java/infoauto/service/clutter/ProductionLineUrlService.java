package infoauto.service.clutter;

import com.baomidou.mybatisplus.extension.service.IService;
import infoauto.entity.ProductionLineUrl;
import java.util.List;


public interface ProductionLineUrlService extends IService<ProductionLineUrl> {
    List<ProductionLineUrl> select();

    int add(ProductionLineUrl plu);
    int updateTab(ProductionLineUrl plu);

    int delete(String productionLineNo);
    List<ProductionLineUrl> selectByProductionLine(String productionLine);
}
