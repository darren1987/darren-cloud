package com.darren.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * gateway网关
 *
 * @author darren.ouyang
 * @version 2018/8/7 20:50
 */
@RestController
@Configuration
@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @RequestMapping("/hystrixfallback")
    public String hystrixfallback() {
        return "This is a fallback";
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("path_route", r -> r.path("/get")
                .uri("http://httpbin.org"))
            .route("host_route", r -> r.host("*.myhost.org")
                .uri("http://httpbin.org"))
            .route("rewrite_route", r -> r.host("*.rewrite.org")
                .filters(f -> f.rewritePath("/foo/(?<segment>.*)",
                    "/${segment}"))
                .uri("http://httpbin.org"))
            .route("hystrix_route", r -> r.host("*.hystrix.org")
                .filters(f -> f.hystrix(c -> c.setName("slowcmd")))
                .uri("http://httpbin.org"))
            .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
                .filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
                .uri("http://httpbin.org"))
            .route("limit_route", r -> r
                .host("*.limited.org").and().path("/anything/**")
                //.filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                .uri("http://httpbin.org"))
            .route("websocket_route", r -> r.path("/echo")
                .uri("ws://localhost:9000"))
            .build();
    }

    //@Bean
    //RedisRateLimiter redisRateLimiter() {
    //    return new RedisRateLimiter(1, 2);
    //}
}
