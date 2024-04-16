package infoauto.util;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import infoauto.entity.ProductionLineUrl;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import java.util.*;

/**
 * Created by InfoAuto.
 *  获取数据库中的产线编号与产线ip，存储到Map里面
 * @author : zhuangweizhong
 * @create 2023/5/11 22:36
 */
@Data
public class UrlUtils extends ProductionLineUrl {

    @Transient
    public static Map<String,String> URLMAP = new HashMap<>();
    @Transient
    public static Map<String,String> LINECODEMAP = new HashMap<>();
    @Transient
    public static List<String> LINECODELIST = new ArrayList<>();


    //解析String
    public static String getUrlConverterString(String productionLineNo){
        return URLMAP.get(productionLineNo);
    }
    //解析List
    public static List<String> getUrlsConverterList(List<String> productionLineNos){
        List<String> list=new ArrayList<>();
        productionLineNos.forEach(item->{
            String url=URLMAP.get(item);
            if (null !=url) {
                list.add(url);
            }
        });
        return list;
    }
    //解析JsonArray数组
    public static List<String> getUrlsConverterJsonArray(JSONArray productionLineNos){
        List<String> list=new ArrayList<>();
        Iterator<Object> iterator = productionLineNos.iterator();
        while (iterator.hasNext()){
            String url=URLMAP.get(iterator.next());
            if (null !=url) {
                list.add(url);
            }
        }
        return list;
    }
}
