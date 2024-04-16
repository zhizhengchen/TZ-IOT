package infoauto.exception;

/**
 * Created by InfoAuto.
 * 打印服务异常
 * @author : zhuangweizhong
 * @create 2023/8/23 9:11
 */
public class PrintServiceException extends RuntimeException{
    private String message;

    public PrintServiceException(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
