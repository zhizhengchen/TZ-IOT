package infoauto.service.clutter;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/11 22:56
 */
public interface UrlService {
   void setProductNoAndUrlForUrlUtils();

   void setProductNoAndZJZLForUrlUtils();

   List<String> getProductionLineNameByNo(List<String> productionLineArrayList);


//   int updateStateForSupplier(int state,List<String> suppliers);
}
