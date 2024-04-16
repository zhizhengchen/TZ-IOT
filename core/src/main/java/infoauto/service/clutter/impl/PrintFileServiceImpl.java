package infoauto.service.clutter.impl;


import infoauto.dao.PrintFileMapper;
import infoauto.entity.PrintFile;
import infoauto.service.clutter.PrintFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by InfoAuto.
 * 打印文件服务
 * @author : zhuangweizhong
 * @create 2023/6/15 14:38
 */
@Service
@Slf4j
public class PrintFileServiceImpl implements PrintFileService {
    @Autowired
    PrintFileMapper mapper;

    @Override
    public List<PrintFile> getPrintFileAll(Integer page, Integer limit) {
        page = (page - 1) * limit;
        List<PrintFile> printFiles = mapper.selectList(page,limit);
        return printFiles;
    }

    /**
     * 新增需要打印的文件路径
     * @param printFile
     * @return
     */
    @Override
    public void insertPrintFile(PrintFile printFile) {
        //新增
        int result = mapper.insert(printFile);
        //插入失败重新插入一次
        if (result==0){
            log.error("插入失败，重新插入");
            //UUID
            String uuid= UUID.randomUUID().toString().replaceAll("-", "");
            printFile.setUuid(uuid);
            mapper.insert(printFile);
        }

    }

    @Override
    public Integer getPrintFileCount() {
        return mapper.getPrintFileCount();
    }

    @Override
    public PrintFile getPrintFileForTaskId(String taskID) {


        return mapper.getPrintFileForTaskId(taskID);
    }

    @Override
    public List<PrintFile> getPrintFileForWorkStation(String workStation) {
        return mapper.selectPrintFileForWorkStation(workStation);
    }


}
