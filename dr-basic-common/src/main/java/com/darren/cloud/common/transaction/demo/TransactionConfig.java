package com.darren.cloud.common.transaction.demo;

import com.darren.cloud.common.transaction.BedtListener;
import com.darren.cloud.common.transaction.TransactionFactory;
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

        TransactionFactory.init(
            new MemoryTransactionDataProvider(),
            new BedtListener(),
            Executors.newSingleThreadScheduledExecutor(),
            10, 5, 10
        );
    }
}
