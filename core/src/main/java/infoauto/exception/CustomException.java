package infoauto.exception;

//自定义异常处理类
public class CustomException extends RuntimeException {
    private String message;

    public CustomException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}