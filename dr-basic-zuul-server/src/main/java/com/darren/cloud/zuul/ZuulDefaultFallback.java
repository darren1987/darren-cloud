package com.darren.cloud.zuul;

import com.alibaba.fastjson.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * 默认zuul路由熔断
 *
 * @author darren.ouyang
 * @version 2018/8/14 18:34
 */
@Slf4j
@Component
public class ZuulDefaultFallback implements FallbackProvider {

    /**
     * 指定要处理的 service。* 或者 null 代表所有服务都过滤
     * @return 路由
     */
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause){
        if (cause != null && cause.getCause() != null) {
            String reason = cause.getCause().getMessage();
            log.info("Excption {}", reason);
        }

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode(){
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText(){
                return  HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                JSONObject json =new JSONObject();
                json.put("state","501");
                json.put("msg","服务不可用");

                //返回前端的内容
                return new ByteArrayInputStream(json.toJSONString().getBytes("UTF-8"));
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }
}
