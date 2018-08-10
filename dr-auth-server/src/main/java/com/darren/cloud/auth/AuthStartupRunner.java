package com.darren.cloud.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动执行
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:46
 */
@Slf4j
@Component
public class AuthStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... arg0) throws Exception {
       log.info(">>>>>>>>>>>>>>>auth服务启动完成<<<<<<<<<<<<<");

    }

}