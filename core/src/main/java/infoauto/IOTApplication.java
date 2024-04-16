package infoauto;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/16 10:15
 */
@SpringBootApplication
@EnableAsync
@MapperScan("infoauto.dao")
@EnableScheduling
@MapperScan(basePackages = {"infoauto.mapper"})
@EnableFeignClients
@ComponentScan(basePackages ={"infoauto.*","com.hvisions.edge.equipment"})
public class IOTApplication {
    public static void main(String[] args) {
        SpringApplication.run(IOTApplication.class, args);
    }
}
