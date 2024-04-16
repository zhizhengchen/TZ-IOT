package infoauto.util;

import infoauto.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/19 14:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageUtil<T> {
    //代码
    private String code;
    //当前页
    private Integer currentPage;
    // 总数
    private Integer count;
    // 列表
    private List<T> returnData;
   //成功结果集
    public static<T> PageUtil<T> success( Integer currentPage, Integer count, List<T> returnData){
        return new PageUtil<>(ResponseEnum.SUCCESS.getCode(),currentPage,count,returnData);
    }
}
