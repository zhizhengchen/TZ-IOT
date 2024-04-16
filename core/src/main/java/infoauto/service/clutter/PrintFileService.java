package infoauto.service.clutter;


import infoauto.entity.PrintFile;

import java.util.List;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/15 14:37
 */
public interface PrintFileService {
    //查询所有打印文件
    List<PrintFile> getPrintFileAll(Integer page, Integer limit);
    //添加打印文件
    void insertPrintFile(PrintFile printFile);
    //获取当前未打印的文件
    Integer getPrintFileCount();

    PrintFile getPrintFileForTaskId(String taskID);

    List<PrintFile> getPrintFileForWorkStation(String workStation);
    //更新当前打印状态
}
