package com.darren.cloud.common;

import com.darren.cloud.common.utils.ClassScanUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.StringUtils;

/**
 * 开发测试环境启动时进行代码规范检查：枚举，task定时任务，缓存key规则检查
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:57
 */
@Slf4j
public class CodeStyleCheck implements ApplicationRunner {

    /** 自定义扫描基础包 */
    private String scanPath = "";

    /** 存放规范异常信息 */
    private List<String> errorMessage = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 获取默认扫描路径
        if (StringUtils.isEmpty(scanPath)) {
            scanPath = this.getDefaultScanPath();
        }

        // 获取包下的所有类
        Set<Class<?>> classes = ClassScanUtils.scan(scanPath);

        if (classes == null || classes.isEmpty()) {
            log.info("工程中没有找到任何类.");
            return;
        }

        errorMessage = new ArrayList<>();

        /*
        // 定时任务类规范检查
        this.taskClassCheck(classes);
        // 枚举类规范检查
        this.enumClassCheck(classes);
        // 只需登录不需鉴权的类规范检查
        this.noAuthCheck(classes);
        // 不需要登陆即可访问的类规范检查
        this.openCheck(classes);
        // 控制层类全局mapping的配置必须包含租户代码占位符
        this.controllerCheck(classes);
        // 检查类是否引用了不规范的包
        this.importPackageCheck(classes);
        // model、param、entity规范检查
        this.beanCheck(classes);
        // 触发器类检查，必须放到trigger包下，且类以Trigger结尾
        this.triggerClassCheck(classes);
        */

        // 打印异常信息
        if (errorMessage.size() >= 1) {
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");

            errorMessage.forEach(log::error);

            log.error("+++++++++++++++++++++++++++++++++++++++++++++");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++");

            System.exit(0);
        }
    }

    /**
     * 获取默认扫描的包路径，最深只返回前3个包
     *
     * @return
     */
    private String getDefaultScanPath() {
        StringBuilder packagePath = new StringBuilder();

        String packageName = this.getClass().getPackage().getName();
        String[] packageNames = packageName.split("\\.");
        if (packageNames.length == 0) {
            return "";
        }

        int len = 0;
        for (String p : packageNames) {
            len++;
            packagePath.append(p).append("/");
            // 最深只返回前3个包
            if (len >= 2) {
                break;
            }
        }

        return packagePath.toString();
    }
}
