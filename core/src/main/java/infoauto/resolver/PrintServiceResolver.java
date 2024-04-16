package infoauto.resolver;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by InfoAuto.
 *  打印异常解析器
 * @author : zhuangweizhong
 * @create 2023/8/23 9:20
 */
public class PrintServiceResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try{
            response.sendError(500,"打印异常");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ModelAndView();
    }
}