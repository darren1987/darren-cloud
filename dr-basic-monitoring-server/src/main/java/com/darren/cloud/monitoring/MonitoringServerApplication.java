package com.darren.cloud.monitoring;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 监控
 *
 * @author darren.ouyang
 * @version 2018/8/10 11:28
 */
@EnableAdminServer
@SpringCloudApplication
public class MonitoringServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringServerApplication.class, args);
    }

}
