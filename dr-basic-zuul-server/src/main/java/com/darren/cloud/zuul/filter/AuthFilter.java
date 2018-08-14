package com.darren.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

/**
 * 接口权限校验，仅对路由转发有效
 *
 * @author darren.ouyang
 * @version 2018/8/9 10:06
 */
@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    private AntPathMatcher antPathMatcher;

    /**
     * 不做权限校验的url
     */
    @Value("${darren-clud.pass-urls}")
    private String[] passUrls;

    /**
     * 不做权限校验的url
     */
    @Value("${darren-clud.pass-login-urls}")
    private String[] passLoginUrls;



    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Override
    public String filterType() {
        //定义filter的类型，有pre、route、post、error四种
        // 前置过滤器
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 优先级为0，数字越大，优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //表示是否需要执行该filter，true表示执行，false表示不执行
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
        if (StringUtils.isEmpty(uri)){
            return false;
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();

        // 经认证中心系统登录后的鉴权token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }

        // 不做权限校验的url, 进行放行
        for (String passUrl : passUrls) {
            if (antPathMatcher.match(passUrl, uri) || antPathMatcher.match(uri, passUrl)){
                ctx.setSendZuulResponse(true);
                ctx.setResponseStatusCode(200);
                return true;
            }
        }

        return false;
    }
}
