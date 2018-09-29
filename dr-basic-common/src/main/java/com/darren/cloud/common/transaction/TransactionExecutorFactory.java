package com.darren.cloud.common.transaction;

import com.darren.cloud.common.transaction.data.TransactionDataProvider;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 事务执行器工厂
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:11
 */
public final class TransactionExecutorFactory {

    /**
     * 是否已初始化
     */
    private static boolean initFlag = false;

    /**
     * 数据存储器
     */
    private static TransactionDataProvider dataProvider;

    /**
     * 线程池服务
     * 场景1 事务提交后默认异步执行事务内所有事件.
     * 场景2 间隔事件扫描未完成事务并事件执行.
     */
    private static ScheduledExecutorService executorService;

    /**
     * 扫描器初始化延迟时间, 单位秒
     */
    private static int SCANNER_INITIAL_DELAY= 60;

    /**
     * 扫描器每次执行间隔时间, 单位秒
     */
    private static int SCANNER_DELAY = 10;

    /**
     * 扫描重试最大次数
     */
    private static int MAX_RETRY_COUNT = 10;

    /**
     * 初始化事务工厂
     *
     * @param dataProvider 数据存储器
     */
    public static void init (
        TransactionDataProvider dataProvider,
        ScheduledExecutorService executorService,
        int scannerInitialDelay,
        int scannerDelay,
        int maxRetryCount
    ){
        TransactionExecutorFactory.dataProvider = dataProvider;
        TransactionExecutorFactory.executorService = executorService;
        TransactionExecutorFactory.SCANNER_INITIAL_DELAY = scannerInitialDelay;
        TransactionExecutorFactory.SCANNER_DELAY = scannerDelay;
        TransactionExecutorFactory.MAX_RETRY_COUNT = maxRetryCount;

        // scannerInitialDelay秒后开始, 每间隔scannerDelay秒执行一次事务扫描器
        executorService.scheduleWithFixedDelay(
            new TransactionScanner(dataProvider),
            TransactionExecutorFactory.SCANNER_INITIAL_DELAY,
            TransactionExecutorFactory.SCANNER_DELAY,
            TimeUnit.SECONDS
        );

        // 标记初始化完成
        TransactionExecutorFactory.initFlag = true;
    }

    /**
     * 创建事务执行器
     *
     * @param remark 信息
     * @return 执行器
     */
    public static TransactionExecutor createExecutor (String remark){
        // 检测是否已初始化
        assertInit();

        TransactionExecutor executor = new TransactionExecutor();
        executor.setDataProvider(dataProvider);
        executor.setExecutorService(executorService);
        executor.setMaxRetryCount(MAX_RETRY_COUNT);
        executor.setEventContainer(new LinkedHashMap<>());
        executor.setTransaction(new TransactionObject()
            .setUuid(UUID.randomUUID().toString().replace("-", "").toLowerCase())
            .setTransactionStatus(TransactionStatusEnum.START.getCode())
            .setRemark(remark)
            .setCreateTime(new Date())
            .setUpdateTime(new Date())
        );

        return executor;
    }

    /**
     * 创建事务执行器
     *
     * @param transaction 事务对象
     * @param eventList 事件集合
     * @return 执行器
     */
    public static TransactionExecutor createExecutor (TransactionObject transaction, List<TransactionEventObject> eventList){
        // 检测是否已初始化
        assertInit();

        TransactionExecutor executor = new TransactionExecutor();
        executor.setDataProvider(dataProvider);
        executor.setExecutorService(executorService);
        executor.setMaxRetryCount(MAX_RETRY_COUNT);
        executor.setEventContainer(new LinkedHashMap<>());
        executor.setTransaction(transaction);

        // 组装事件容器对象
        for (TransactionEventObject event : eventList){
            TransactionEvent eventFunction = TransactionEventManager.createEvent(event);
            executor.getEventContainer().put(event, eventFunction);
        }
        return executor;
    }

    /**
     * 断言是否初始化
     */
    private static void assertInit (){
        if (!initFlag){
            throw new RuntimeException("TransactionExecutorFactory error, please init first!!!");
        }
    }
}
