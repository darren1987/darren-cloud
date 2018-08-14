package com.darren.cloud.user;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 用户服务入口程序
 *
 * @author darren.ouyang
 * @version 2018/8/8 16:01
 */
@EnableScheduling
@SpringCloudApplication
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }
}
