package com.darren.cloud.common.transaction.demo;

import com.darren.cloud.common.transaction.TransactionExecutor;
import com.darren.cloud.common.transaction.TransactionExecutorFactory;
import com.darren.cloud.common.utils.JsonUtils;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomUtils;

/**
 * 生产端 demo
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:28
 */
public class DemoProducerServer {

    public static void main(String[] args){

        TransactionConfig.init();
        (new DemoProducerServer()).demo1();
    }

    //@Transactional
    public void demo1 (){

        // 模拟本地创建订单
        DemoOrder order = insertOrder();
        System.out.println("本地保存: 订单. order: " + JsonUtils.toJson(order));

        TransactionExecutor executor = TransactionExecutorFactory.createExecutor("demo1");
        executor.start();
        executor.sendEvent(DemoBillTransactionEvent.of("店长darren", order));
        executor.sendEvent(DemoWechatTransactionEvent.of("买家小花", order.getName()));
        executor.commit();
    }

    /**
     * 模拟本地创建订单
     *
     * @return 订单
     */
    private DemoOrder insertOrder() {
        int id = autoIncrementId.incrementAndGet();
        return new DemoOrder()
            .setId(id)
            .setName("买买买Number:" + id)
            .setAmount(new BigDecimal(RandomUtils.nextInt(100, 1000)))
            ;
    }

    private static final AtomicInteger autoIncrementId = new AtomicInteger(0);

    @Data
    @Accessors(chain = true)
    public static class DemoOrder {
        private int id;             // 订单ID
        private String name;        // 订单名称
        private BigDecimal amount;  // 订单金额
    }
}
