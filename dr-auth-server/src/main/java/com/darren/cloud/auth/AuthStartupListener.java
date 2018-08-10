package com.darren.cloud.auth;

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
public class AuthStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent arg0) {
        log.info(">>>>>>>>>>>>>>auth服务启动监听器<<<<<<<<<<<<<");
    }
}
