package infoauto.util;

import infoauto.entity.PrintParameterConfig;
import infoauto.service.clutter.PrintParameterConfigService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.awt.print.Paper;

/**
 * Created by InfoAuto.
 * 打印相关参数设置
 * @author : zhuangweizhong
 * @create 2023/6/15 10:15
 */
@Data
public class PrintParameter {
    //打印参数service
    @Autowired
    private PrintParameterConfigService service;

    //创建类
    public static PrintParameter builder(){
        return new PrintParameter();
    }

    /**
     * 设置打印份数
     *
     * @param copy
     * @return
     */
    public static PrintRequestAttributeSet getPrintRequestAttributeSet(int copy) {
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(copy)); //份数
        aset.add(MediaSizeName.ISO_A4); //纸张
        // aset.add(Finishings.STAPLE);//装订
        aset.add(Sides.ONE_SIDED);//单双面
        return aset;
    }
    //打印属性设置
    public static PrintRequestAttributeSet getPrintRequestAttributeSet() {

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(1)); //份数
//        aset.add(MediaSizeName.ISO_A6); //纸张
        // aset.add(Finishings.STAPLE);//装订
        aset.add(Sides.ONE_SIDED);//单双面
        return aset;
    }

    /**
     * 标签尺寸默认设置
     * @return
     */
    public static Paper paperSetDefault() {
        Paper paper = new Paper();
        //原先+0.5再乘以28
        double width = 182.0;
        //原先+1.5再乘以28
        double height = 154.0;
//        // 设置边距，单位是像素，10mm边距，对应 28px
        double marginLeft = 0.0;
        double marginRight = 0.0;
        double marginTop = 0.0;
        double marginBottom = 0.0;

        paper.setSize(width, height);
        // 下面一行代码，解决了打印内容为空的问题
        paper.setImageableArea(marginLeft,marginRight,width-(marginLeft+marginRight), height-(marginTop+marginBottom));
        return paper;

    }

    /**
     * 标签尺寸自定义设置
     */
    public  Paper paperSetCustom(String workStation){
        //根据工位编号查询参数值
        Paper paper = new Paper();
        PrintParameterConfig p=service.selectPrintParameterConfigForWorkStation(workStation);
        paper.setSize(p.getWidth(), p.getHeight());
        paper.setImageableArea(p.getMarginLeft(),p.getMarginRight(),p.getWidth()-(p.getMarginLeft()+p.getMarginRight()), p.getHeight()-(p.getMarginTop()+p.getMarginBottom()));
        return paper;
    }

}
