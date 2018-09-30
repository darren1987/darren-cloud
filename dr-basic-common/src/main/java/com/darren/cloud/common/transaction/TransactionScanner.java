package com.darren.cloud.common.transaction;

import com.darren.cloud.common.transaction.data.TransactionDataProvider;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 事务扫描器
 *
 * @author darren.ouyang
 * @version 2018/9/26 16:28
 */
public class TransactionScanner implements Runnable {

    /**
     * 数据存储供应对象
     */
    private final TransactionDataProvider transactionDataProvider;

    TransactionScanner (TransactionDataProvider transactionDataProvider){
        this.transactionDataProvider = transactionDataProvider;
    }

    @Override
    public void run (){
        Map<TransactionObject, List<TransactionEventObject>> map = transactionDataProvider.findMapOnCommit();
        for (Entry<TransactionObject, List<TransactionEventObject>> entry : map.entrySet()){
            BedtExecutor executor = TransactionFactory.createBedtExecutor(entry.getKey(), entry.getValue());
            executor.executeTransaction();
        }
    }
}
