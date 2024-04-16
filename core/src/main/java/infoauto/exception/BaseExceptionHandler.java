package infoauto.exception;

import infoauto.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice// 针对所有的RestController添加了AOP（AOP切入全局异常处理）
@Slf4j // 开启日志
public class BaseExceptionHandler {
/*
    @ExceptionHandler(value = Exception.class) //捕获所有的异常
    public Result handleException(Exception e){
        // 使用日志记录异常
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length > 0 && stackTrace[0] != null) {
            StackTraceElement traceElement = stackTrace[0];
            String fileName = traceElement.getFileName();
            String methodName = traceElement.getMethodName();
            int lineNumber = traceElement.getLineNumber();
            log.error("------------出现异常--------------");
            log.error("异常位置:{},方法{},第{}行", fileName, methodName, lineNumber);
            log.error("异常信息:{}", e.toString());
        }
        e.printStackTrace();
        return Result.error("链接超时");
    }*/
}
