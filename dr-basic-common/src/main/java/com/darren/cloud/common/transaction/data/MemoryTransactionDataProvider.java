package com.darren.cloud.common.transaction.data;

import com.darren.cloud.common.transaction.TransactionEventObject;
import com.darren.cloud.common.transaction.TransactionObject;
import com.darren.cloud.common.transaction.TransactionStatusEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  基于本地内存的事务存储器
 *  请勿用于生产环境, 停止服与本地事务无法确保数据安全
 *
 * @author darren.ouyang
 * @version 2018/9/27 19:34
 */
public class MemoryTransactionDataProvider implements TransactionDataProvider {

    /**
     * 事务Map
     */
    private Map<String, TransactionObject> transactionMap = new HashMap<>();

    /**
     * 事务事件Map
     */
    private Map<String, Set<TransactionEventObject>> transactionEventsMap = new HashMap<>();

    @Override
    public synchronized void insertTransaction(TransactionObject transaction) {
        transactionMap.put(transaction.getUuid(), transaction);
    }

    @Override
    public synchronized void updateTransaction(TransactionObject transaction) {
        transactionMap.put(transaction.getUuid(), transaction);
    }

    @Override
    public synchronized void updateTransactionToFinish(TransactionObject transaction) {
        transactionMap.remove(transaction.getUuid());
    }

    @Override
    public synchronized void insertEvent(TransactionEventObject event) {
        Set<TransactionEventObject> set = transactionEventsMap
            .computeIfAbsent(event.getUuid(), k -> new HashSet<>());

        set.add(event);
    }

    @Override
    public synchronized void updateEvent(TransactionEventObject event) {
        Set<TransactionEventObject> set = transactionEventsMap.get(event.getUuid());
        if (set == null){
            return;
        }

        set.add(event);
    }

    @Override
    public synchronized Map<TransactionObject, List<TransactionEventObject>> findMapOnCommit() {

        Map<TransactionObject, List<TransactionEventObject>> resultMap = new HashMap<>();
        for (TransactionObject object : transactionMap.values())
        {
            if (!TransactionStatusEnum.COMMIT.equalsCode(object.getTransactionStatus())){
                continue;
            }

            Set<TransactionEventObject> set = transactionEventsMap.get(object.getUuid());
            if (set == null){
                continue;
            }

            resultMap.put(object, new ArrayList<>(set));
        }

        return resultMap;
    }
}
