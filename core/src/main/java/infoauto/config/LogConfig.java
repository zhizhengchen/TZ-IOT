package infoauto.config;

import infoauto.Interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by InfoAuto.
 *统一操作日志配置，将拦截器注入并配置拦截路径
 * @author : zhuangweizhong
 * @create 2023/5/11 11:05
 */
@Configuration
public class LogConfig implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }
}
