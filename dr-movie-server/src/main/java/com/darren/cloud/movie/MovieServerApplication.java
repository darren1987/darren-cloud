package com.darren.cloud.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 电影服务入口程序
 *
 * @author darren.ouyang
 * @version 2018/8/8 16:01
 */
@EnableScheduling
@SpringCloudApplication
public class MovieServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieServerApplication.class, args);
    }
}
