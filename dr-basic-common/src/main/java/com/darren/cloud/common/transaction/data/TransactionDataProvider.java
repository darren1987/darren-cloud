package com.darren.cloud.common.transaction.data;

import com.darren.cloud.common.transaction.TransactionEventObject;
import com.darren.cloud.common.transaction.TransactionObject;
import java.util.List;
import java.util.Map;

/**
 * 数据存储器
 *
 * @author darren.ouyang
 * @version 2018/9/26 16:10
 */
public interface TransactionDataProvider {

    /**
     * 新增事务
     *
     * @param transaction 事务对象
     */
    void insertTransaction(TransactionObject transaction);

    /**
     * 更新事务
     *
     * @param transaction 事务
     */
    void updateTransaction (TransactionObject transaction);

    /**
     * 完成事务
     *
     * @param transaction 事务
     */
    void updateTransactionToFinish (TransactionObject transaction);

    /**
     * 新增事件
     *
     * @param event 事件对象
     */
    void insertEvent(TransactionEventObject event);

    /**
     * 更新事件
     *
     * @param event 事件对象
     */
    void updateEvent (TransactionEventObject event);

    /**
     * 获得在提交状态的事务对象与事件集合
     *
     * @return map
     */
    Map<TransactionObject, List<TransactionEventObject>> findMapOnCommit ();
}