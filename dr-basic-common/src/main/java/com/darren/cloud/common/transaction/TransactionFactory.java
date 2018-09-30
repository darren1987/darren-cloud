package com.darren.cloud.common.transaction;

import com.darren.cloud.common.transaction.data.TransactionDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 事务工厂
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:11
 */
public final class TransactionFactory {

    /**
     * 是否已初始化
     */
    private static boolean initFlag = false;

    /**
     * 数据存储器
     */
    private static TransactionDataProvider dataProvider;

    /**
     * 监听器
     */
    private static BedtListener listener;

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
        BedtListener listener,
        ScheduledExecutorService executorService,
        int scannerInitialDelay,
        int scannerDelay,
        int maxRetryCount
    ){
        TransactionFactory.dataProvider = dataProvider;
        TransactionFactory.listener = listener;
        TransactionFactory.executorService = executorService;
        TransactionFactory.SCANNER_INITIAL_DELAY = scannerInitialDelay;
        TransactionFactory.SCANNER_DELAY = scannerDelay;
        TransactionFactory.MAX_RETRY_COUNT = maxRetryCount;

        // scannerInitialDelay秒后开始, 每间隔scannerDelay秒执行一次事务扫描器
        executorService.scheduleWithFixedDelay(
            new TransactionScanner(dataProvider),
            TransactionFactory.SCANNER_INITIAL_DELAY,
            TransactionFactory.SCANNER_DELAY,
            TimeUnit.SECONDS
        );

        // 标记初始化完成
        TransactionFactory.initFlag = true;
    }

    /**
     * 创建最大努力型分布式事务执行器
     *
     * @param remark 信息
     * @return 执行器
     */
    public static BedtExecutor createBedtExecutor(String remark){
        // 检测是否已初始化
        assertInit();

        TransactionObject transaction = new TransactionObject()
            .setUuid(UUID.randomUUID().toString().replace("-", "").toLowerCase())
            .setTransactionStatus(TransactionStatusEnum.START.getCode())
            .setRemark(remark)
            .setErrorInfo("");

        return new BedtExecutor()
            .setDataProvider(dataProvider)
            .setListener(listener)
            .setExecutorService(executorService)
            .setMaxRetryCount(MAX_RETRY_COUNT)
            .setEventContainer(new ArrayList<>())
            .setTransaction(transaction);
    }

    /**
     * 创建事务执行器
     *
     * @param transaction 事务对象
     * @param eventList 事件集合
     * @return 执行器
     */
    public static BedtExecutor createBedtExecutor(TransactionObject transaction, List<TransactionEventObject> eventList){
        // 检测是否已初始化
        assertInit();

        return new BedtExecutor()
            .setDataProvider(dataProvider)
            .setListener(listener)
            .setExecutorService(executorService)
            .setMaxRetryCount(MAX_RETRY_COUNT)
            .setEventContainer(eventList)
            .setTransaction(transaction);
    }

    /**
     * 断言是否初始化
     */
    private static void assertInit (){
        if (!initFlag){
            throw new RuntimeException("TransactionFactory error, please init first!!!");
        }
    }
}
