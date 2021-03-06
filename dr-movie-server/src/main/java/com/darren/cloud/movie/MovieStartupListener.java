package com.darren.cloud.movie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 启动监听器
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:48
 */
@Slf4j
@Component
public class MovieStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent arg) {
        log.info(">>>>>>>>>>>>>>movie服务启动监听器<<<<<<<<<<<<<");
    }
}
