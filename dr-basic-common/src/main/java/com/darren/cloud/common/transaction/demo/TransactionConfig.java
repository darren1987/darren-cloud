package com.darren.cloud.common.transaction.demo;

import com.darren.cloud.common.transaction.TransactionExecutorFactory;
import com.darren.cloud.common.transaction.data.MemoryTransactionDataProvider;
import java.util.concurrent.Executors;

/**
 * 事务配置类
 *
 * @author darren.ouyang
 * @version 2018/9/26 17:33
 */
//@Configuration
public class TransactionConfig {

    public static void init (){

        TransactionExecutorFactory.init(
            new MemoryTransactionDataProvider(),
            Executors.newSingleThreadScheduledExecutor(),
            10, 5, 10
        );
    }
}
