package infoauto.config;

import infoauto.service.clutter.UrlService;
import infoauto.service.zp.ZkUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Created by InfoAuto.
 * 启动后自动调用查询产线编号与ip的语句，存储至map
 * @author : zhuangweizhong
 * @create 2023/5/11 23:35
 */
@Configuration
public class SysConfig implements CommandLineRunner {
    @Autowired
    UrlService urlService;

    @Autowired
    ZkUploadService zkUploadService;

    @Override
    public void run(String... args) throws Exception {

        urlService.setProductNoAndUrlForUrlUtils();

        urlService.setProductNoAndZJZLForUrlUtils();


        zkUploadService.registerEquipment();
    }
}
