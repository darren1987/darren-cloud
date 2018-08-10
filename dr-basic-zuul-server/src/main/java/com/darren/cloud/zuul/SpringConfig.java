package com.darren.cloud.zuul;

import com.darren.cloud.common.filter.RequestLoggingFilter;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * spring相关配置
 *
 * @author darren.ouyang
 * @version 2018/8/9 12:39
 */
@Configuration
public class SpringConfig implements ErrorPageRegistrar {



    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));
        registry.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html"));
        registry.addErrorPages(new ErrorPage(Throwable.class, "/500.html"));
    }

    /**
     * 打印请求信息的过滤器
     */
    @Bean
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }
}
