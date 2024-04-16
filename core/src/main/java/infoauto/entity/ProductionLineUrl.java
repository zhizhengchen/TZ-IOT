package infoauto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("production_line_url")
public class ProductionLineUrl {
//    @Column(value = "id")
//    private Integer id;
    @Column(value = "production_line_no")
    private String productionLineNo;
    @Column(value = "url")
    private String url;
    @Column(value = "state")
    private int state;
    //供应商
    @Column(value ="supplier")
    private String supplier;
}
