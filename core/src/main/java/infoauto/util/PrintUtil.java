package infoauto.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import infoauto.enums.PaperType;
import infoauto.exception.PrintServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.springframework.data.annotation.Transient;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.PrintRequestAttributeSet;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by InfoAuto.
 * 打印工具类
 * @author : zhuangweizhong
 * @create 2023/6/8 15:51
 * 最新更新2023/8/22 09:26
 */

public class PrintUtil {

    @Transient
    public static Map<String,String> PRINTMAP=new HashMap<>();

    /**
     * 寻找指定的打印机
     * @param printerName
     * @return
     */
    public static PrintService lookupPrinter(String printerName) throws Exception {
        //打印服务设置为空
        PrintService service = null;
        //打印服务集合设置为空
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        if (printServices == null || printServices.length == 0) {
            throw new Exception("未获取到打印服务");
        }
        //通过遍历打印服务获取打印服务的名称，添加到集合里面==》打印名称集合
        List<String> printerNames = Arrays.stream(printServices).map(p -> p.getName()).collect(Collectors.toList());

        for (int i = 0; i < printServices.length; i++) {
            String name = printServices[i].getName().toLowerCase();
            //log.info("printName{}:{}", i, name);
            //判断传入的打印服务名称是否属于  打印服务里面的名称，如果存在设置添加到打印
            if (name.contains(printerName.toLowerCase())) {
                service = printServices[i];
                break;
            }
        }
        //若service为空，则抛出打印异常，项目需要找不到打印服务时将内容记录
        if (service == null) {
            throw new PrintServiceException("未找到指定的打印机："+printerName);
//            throw new Exception("未找到指定的打印机:" + printerName + ",可选打印服务:" + printerNames);

        }
        return service;
    }

    /**
     * 通过文件路径，打印服务名称，以及打印属性设置对象进行打印，符合当前项目场景，通过文件路径调用打印机进行打印
     * @param filePath
     * @param printerName
     * @param aset
     * @throws Exception
     */
    public static void print(String filePath, String printerName, PrintRequestAttributeSet aset, PrintParameter printParameter, PaperType paperType) throws Exception {
        print(new File(filePath), printerName, aset,printParameter,paperType);
    }

    /**
     * 打印指定文件
     * @param file
     * @param printerName
     * @param aset 打印属性,可通过这个设置打印份数
     * @throws Exception
     */

    public static void print(File file, String printerName, PrintRequestAttributeSet aset,PrintParameter printParameter, PaperType paperType,String... workStation) throws Exception {
        //判断文件是否存在
        if (file == null) {
            //log.error("传入的文件为空");
            throw new Exception("传入的文件为空");
        }
        if (!file.exists()) {
            //log.error("文件不存在:" + file.getAbsolutePath());
            throw new Exception("文件不存在:" + file.getAbsolutePath());
        }
        //设置打印服务名称
        PrintService printService = lookupPrinter(printerName);
        if (null == aset) {
            aset = printParameter.getPrintRequestAttributeSet(); // 获取打印参数
        }

        PDDocument document = null;
        PrinterJob printJob=null;
        try{
        //加载文件
        document = PDDocument.load(file);

        // 开启打印任务
        printJob = PrinterJob.getPrinterJob();
        //设置打印任务的名称
        printJob.setJobName(file.getName());
        //选择打印机

        printJob.setPrintService(printService);

        //设置纸张及缩放,等比缩放
        PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.SHRINK_TO_FIT);
        //设置多页打印
        Book book = new Book();

        PageFormat pageFormat = new PageFormat();
        //设置打印方向
        //纵向PageFormat.PORTRAIT
        pageFormat.setOrientation(PageFormat.PORTRAIT);
            //设置纸张,为空则使用默认值
            String typeName = paperType.getTypeName();
            switch (typeName){
                case "Default": pageFormat.setPaper(printParameter.paperSetDefault()); break;
                case "Custom" : pageFormat.setPaper(printParameter.paperSetCustom(workStation[0]));break;
            }
        book.append(pdfPrintable, pageFormat, document.getNumberOfPages());
        printJob.setPageable(book);
            printJob.print(aset);
        }catch (IOException e){
            e.printStackTrace();
//            throw  new Exception("传输异常"+e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            //关闭文件
            IOUtils.closeQuietly(document);
        }
    }


//    /**
//     * 将图片转换成pdf关闭文件
//     *
//     * @return
//     * @throws Exception
//     */
//    public static byte[] img2PDF(List<byte[]> images) throws Exception {
//
//        Document doc = new Document(PageSize.A4, 0, 0, 0, 0);//普通a4
//        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
//        PdfWriter.getInstance(doc, pdfOut);
//        doc.open();
//        for (byte[] image : images) {
//            com.itextpdf.text.Image pic = com.itextpdf.text.Image.getInstance(image);
//            pic.setScaleToFitLineWhenOverflow(true);
//            doc.add(pic);
//        }
//
//        doc.close();
//        byte[] pdf = pdfOut.toByteArray();
//        IOUtils.closeQuietly(pdfOut);
//        return pdf;
//    }
//
//    /**
//     * 图片缩放成A4尺寸,转换为pdf文件
//     *
//     * @param imagePath
//     * @param descfolder
//     * @return
//     * @throws Exception
//     */
//    public static String img2PDF(String imagePath, String descfolder, String pdfName) throws Exception {
//        return img2PDF(Arrays.asList(imagePath), descfolder, pdfName);
//    }
//
//    /**
//     * 图片缩放成A4尺寸,转换为pdf文件
//     *
//     * @param imgPaths 图片路径
//     * @param descfolder
//     * @return
//     * @throws Exception
//     */
//    public static String img2PDF(List<String> imgPaths, String descfolder) throws Exception {
//        //设置PDF名称，系统时间秒数+.pdf后缀
//        String pdfName = System.currentTimeMillis() + ".pdf";
//
//        return img2PDF(imgPaths, descfolder, pdfName);
//    }
//    /**
//     * 批量将图片生成pdf
//     * @param imgPaths
//     * @param descfolder
//     * @param pdfName
//     * @return
//     * @throws Exception
//     */
//    public static String img2PDF(List<String> imgPaths, String descfolder, String pdfName) throws Exception {
//        //判断文件名是否存在，如果不存在，则添加新文件名，如果存在则覆盖
//        pdfName = StringUtils.isEmpty(pdfName) ? System.currentTimeMillis() + ".pdf" : pdfName;
//        String pdfPath = "";
//        //文件输出流
//        FileOutputStream fos = null;
//        try {
//            //文件夹获取
//            File file = new File(descfolder);
//            //文件夹不存在
//            if (!file.exists()) {
//                //创建新的文件夹
//                file.mkdirs();
//            }
//            //文件全路径
//            pdfPath = descfolder + "\\\\" + pdfName;
//            //打印属性设置，A4值，边距设置
//            Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
//            //读取文件
//            fos = new FileOutputStream(pdfPath);
//            //将文件写入到新的文件夹下的文件
//            PdfWriter.getInstance(doc, fos);
//            //文档开启
//            doc.open();
//            //遍历图片地址
//            for (String imagePath : imgPaths) {
//                //图片流
//                com.itextpdf.text.Image image = image = com.itextpdf.text.Image.getInstance(imagePath);
//                //设置绝对定位
//                image.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
//                //文档添加图片
//                doc.add(image);
//            }
//            //文档关闭
//            doc.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //log.error("生成pdf异常:" + e.getMessage());
//            throw new Exception("生成pdf异常:" + e.getMessage());
//        } finally {
//            IOUtils.closeQuietly(fos);
//        }
//        return pdfPath;
//    }
//    /**
//     * 批量将图片生成pdf
//     * @param imgPaths
//     * @param descfolder
//     * @param pdfName
//     * @return
//     * @throws Exception
//     */
//    public static String img3PDF(List<String> imgPaths, String descfolder, String pdfName) throws Exception {
//        //判断文件名是否存在，如果不存在，则添加新文件名，如果存在则覆盖
//        pdfName = StringUtils.isEmpty(pdfName) ? System.currentTimeMillis() + ".pdf" : pdfName;
//        String pdfPath = "";
//        //文件输出流
//        FileOutputStream fos = null;
//        try {
//            //文件夹获取
//            File file = new File(descfolder);
//            //文件夹不存在
//            if (!file.exists()) {
//                //创建新的文件夹
//                file.mkdirs();
//            }
//            //文件全路径
//            pdfPath = descfolder + "\\\\" + pdfName;
//            //打印属性设置，A4值，边距设置
//            Document doc = new Document(PageSize.B8, 0, 0, 0, 0);
//            //读取文件
//            fos = new FileOutputStream(pdfPath);
//            //将文件写入到新的文件夹下的文件
//            PdfWriter.getInstance(doc, fos);
//            //文档开启
//            doc.open();
//            //遍历图片地址
//            for (String imagePath : imgPaths) {
//                //图片流
//                com.itextpdf.text.Image image = image = com.itextpdf.text.Image.getInstance(imagePath);
//                //设置绝对定位
//                image.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
//                //文档添加图片
//                doc.add(image);
//            }
//            //文档关闭
//            doc.close();
//            //log.info("生成pdf成功:{}", pdfPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //log.error("生成pdf异常:" + e.getMessage());
//            throw new Exception("生成pdf异常:" + e.getMessage());
//        } finally {
//            IOUtils.closeQuietly(fos);
//        }
//        return pdfPath;
//    }
}
