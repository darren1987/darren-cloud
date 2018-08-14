package com.darren.cloud.scheduling;

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
public class SchedulingStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
       log.info(">>>>>>>>>>>>>>>scheduling服务启动完成<<<<<<<<<<<<<");

    }

}