package infoauto.filter;

import infoauto.util.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/15 16:28
 */
@Slf4j
@Component
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(requestWrapper,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
