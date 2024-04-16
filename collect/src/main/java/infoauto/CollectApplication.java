package infoauto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/8/3 11:48
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages ={"infoauto.*","com.hvisions.edge.equipment"})
public class CollectApplication {
    public static void main(String[] args) {
            SpringApplication.run(CollectApplication.class);
    }

}
