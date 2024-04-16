package infoauto.config;


import infoauto.service.GatherEquipmentService;
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
    GatherEquipmentService gatherEquipmentService;

    @Override
    public void run(String... args) throws Exception {
        //查询所有的设备
        gatherEquipmentService.registerEquipment();
        gatherEquipmentService.andonEquipment();
    }
}

