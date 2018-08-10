package com.darren.cloud.common.filter;

import com.darren.cloud.common.utils.RequestUtils;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求信息打印过滤器
 *
 * @author darren.ouyang
 * @version 2018/8/9 12:25
 */
@Slf4j
public class RequestLoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final BodyReaderWrapper wrapper = new BodyReaderWrapper((HttpServletRequest) request);
        String requestMessage = RequestUtils.getRequestMessage(wrapper);
        log.info(requestMessage);
        chain.doFilter(wrapper, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}


}

