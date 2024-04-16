package infoauto.Interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import infoauto.anno.MyLog;
import infoauto.util.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/11 10:48
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    /**
     * 校验是否为UTF-8的JSON数据
     * @param request
     * @return
     */
    private boolean isJson(HttpServletRequest request) {
        if (request.getContentType()!=null){
            return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ||
                    request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE);

        }
        return false;
    }
    /**
     * 在执行servlet的service方法之前执行
     * 在进入controller之前调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        String bodyString = new RequestWrapper(request).getBody();
        String ip=request.getRemoteAddr();
        if (isJson(request)&& StringUtils.isNotEmpty(bodyString)){
            if (handler instanceof ResourceHttpRequestHandler){

            }else {
                this.logSaveForMySQl((HandlerMethod) handler,bodyString,ip);
            }
            }
        return true;
    }
    /**
     * 在执行完controller之后，返回视图之前执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 在整个请求结束后，返回视图之后执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
    /**
     * 将日志保存到ElasticSearch或数据库
     * @param handlerMethod
     * @param stringJson
     */
    public void logSaveForMySQl(HandlerMethod handlerMethod,String stringJson,String ip){
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        Method method = handlerMethod.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation:annotations){
                if(annotation instanceof MyLog){
                    JSONObject headerJsonObject = JSON.parseObject(stringJson).getJSONObject("header");
                    if(null!=headerJsonObject){
                        String type = ((MyLog) annotation).type().getName();
                        //仅存储business_log
                        if (null!=type&&type.equals("business_log")){
                            String taskType = headerJsonObject.get("taskType").toString();
                            String version = headerJsonObject.get("version").toString();
                            String taskId = headerJsonObject.get("taskId").toString();
                            String direction="MySQL";
                            //以下是封装存储多个功能
//                            SaveDirection[] directions = ((MyLog) annotation).directions();
//                            String direction ="";
//                            for (SaveDirection d : directions) {
//                                direction+=d.getName();
//                            }
//                            version="ip:"+ip+"  版本："+version;
                            log.info("|{}|{}|{}|{}|{}|{}|{}", type ,direction,version,taskId,taskType,stringJson,time,ip);
                        }
                    }
//                    else{
//                        JSONObject headerJsonObject2 = JSON.parseObject(stringJson).getJSONObject("In");
//                    }
                }else {
                    break;
                }
            }
        }
    }
}
