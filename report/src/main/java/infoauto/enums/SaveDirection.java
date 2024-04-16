package infoauto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by InfoAuto.
 * 存储目标，注意，本系统只开放ElasticSearch
 * @author : zhuangweizhong
 * @create 2023/5/15 17:37
 */
@Getter
@AllArgsConstructor
public enum SaveDirection {
    ELASTICSEARCH("ElasticSearch","日志保存到ElasticSearch,当前项目无该需求，使用会报错"),
    MYSQL("MySQL","日志保存到MySQL"),
    PGSQL("PgSQL","日志保存到PgSQL,当前项目无该需求，使用会报错");
    private String name;
    private String content;
}
