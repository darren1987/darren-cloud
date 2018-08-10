package com.darren.cloud.auth.task;

import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * demo定时任务
 * @author darren.ouyang
 * @version 2018/8/10 16:36
 */
@Slf4j
@Component
public class DemoAuthTask {

    private AtomicLong numb = new AtomicLong(0);

    @Scheduled(cron="*/5 * * * * ?")
    public void runFunction() {
        log.info("ε=(´ο｀*)))唉 {}", numb.incrementAndGet());
    }
}
