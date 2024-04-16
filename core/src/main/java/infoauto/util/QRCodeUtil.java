package infoauto.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import infoauto.enums.PaperType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//import org.apache.commons.net.util.Base64;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/6/13 16:05
 */

public class QRCodeUtil {

    //二维码的宽高
    private static final int WIDTH=200;

    private static final int HEIGHT=200;
    // 加文字二维码高
    private static final int WORDHEIGHT = 250;

    /**
     *  生成二维码图片
     * @param content 二维码的内容
     * @return
     */
    public static BufferedImage createQRCode(String content) {

        Map<EncodeHintType, Object> hints= new HashMap<EncodeHintType, Object>();
        //容错级别L>M>Q>H(级别越高扫描时间越长)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        //字符编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);//白边的宽度，可取0~4
        BufferedImage image=null;
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            //生成矩阵数据
            BitMatrix bitMatrix=multiFormatWriter.encode(content,
                    BarcodeFormat.QR_CODE,WIDTH,HEIGHT,hints);
            int w = bitMatrix.getWidth();
            int h = bitMatrix.getHeight();
            //生成二维码bufferedImage图片
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ?
                            MatrixToImageConfig.BLACK : MatrixToImageConfig.WHITE);
                }
            }


        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败："+e.getMessage());
        }
        return image;

    }

    /**
     *  生成解析二维码
     * @param filePath 二维码的内容
     * @return
     */
    public static Result parseQRCode(String filePath) {
        //二维码编码相关的参数
        Map<DecodeHintType, Object> hints= new HashMap<DecodeHintType, Object>();
        //字符编码
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

        BufferedImage image=null;
        try {
            File file = new File(filePath);
            image = ImageIO.read(file);

            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            //解析二维码
            Result result = new MultiFormatReader().decode(bitmap, hints);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("解析二维码失败："+e.getMessage());
        }

    }

    /**
     *  删除生成的二维码周围的白边，根据审美决定是否删除
     * @param matrix BitMatrix对象
     * @return BitMatrix对象
     * */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    /**
     * 二维码底部加上文字
     * @param image 传入二维码对象
     * @param words 显示的文字内容
     * @return
     */
    private static BufferedImage insertWords(BufferedImage image,String words){
        BufferedImage outImage = null;
        if (  StringUtils.isNotBlank(words)){
            outImage= new BufferedImage(WIDTH, WORDHEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D outg = outImage.createGraphics();
            // 画二维码到新的面板
            outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            // 画文字到新的面板
            outg.setColor(Color.BLACK);
            outg.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 字体、字型、字号
            int strWidth = outg.getFontMetrics().stringWidth(words);
            int singleFD=outg.getFontMetrics().stringWidth("2");
            int grow=WIDTH/singleFD-1;
            //长度过长就截取超出二维码宽度部分换行
            if (strWidth > WIDTH) {
                String serialnum1 = words.substring(0, grow);
                String serialnum2 = words.substring(grow, words.length());
                outg.drawString(serialnum1, 5, image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 );
                BufferedImage outImage2 = new BufferedImage(WIDTH, WORDHEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D outg2 = outImage2.createGraphics();
                outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
                outg2.setColor(Color.BLACK);
                // 字体、字型、字号
                outg2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                //参数：显示的内容、起始位置x、起始的y
                outg2.drawString(serialnum2,5, outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 );
                outg2.dispose();
                outImage2.flush();
                outImage = outImage2;
            } else {
                // 画文字
                outg.drawString(words, (WIDTH - strWidth) / 2,
                        image.getHeight() + (outImage.getHeight() - image.getHeight()) / 3 );
            }
            outg.dispose();
            outImage.flush();
            image.flush();
        }

        return outImage;
    }


    /**
     * 生成二维码下方不带文字
     * @return
     */
    public static String getQRCode(String srialnum,String savePath) {
        BufferedImage image=createQRCode(srialnum);
        try {

            //字节数组流包装输出对象流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);//将图片写入到输出流
            ImageIO.write(image, "png", new File(savePath));//直接写入某路径
            return Base64.encodeBase64URLSafeString(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("写出图片流失败");
        }

    }

    /**
     * 生成的二维码下方带文字
     * @return
     */
    public static String getQRCode(String srialnum,String words,String savePath) {

        BufferedImage image = insertWords(createQRCode(srialnum), words);
        try {

            //字节数组流包装输出对象流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.flush();
            //将图片写入到输出流
            ImageIO.write(image, "png", baos);

            ImageIO.write(image, "png", new File(savePath));//直接写入某路径
            return Base64.encodeBase64URLSafeString(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("写出图片流失败!!!");
        }

    }

    public static void main(String[] args) throws Exception {
//        String srialnum="abcdefg123435657";
//        String savePath="D:\\66.jpg";
////        getQRCode(srialnum,words,savePath);
//        getQRCode(srialnum,savePath);
//        ArrayList<String> imgs = new ArrayList<>();
//
//        imgs.add(savePath);
//        String s = PrintUtil.img2PDF(imgs, "D:", "66.pdf");
//        System.out.println(s);
        PrintUtil.print("D:\\1.pdf","Honeywell PX240",PrintParameter.getPrintRequestAttributeSet(),PrintParameter.builder(), PaperType.DEFAULT);
//		getQRCode(srialnum,savePath);
//        System.out.println("ok");

//        去掉固定长度字符串的空格问题
//        String string= "       uiiuhi kkjsnf jkjk  ";
    }



}

