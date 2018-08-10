package com.darren.cloud.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动完成监听器
 *
 * @author darren.ouyang
 * @version 2018/7/9 13:56
 */
@Slf4j
@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>>>>>>>>>>>>注册中心启动完成<<<<<<<<<<<<<");
    }
}
