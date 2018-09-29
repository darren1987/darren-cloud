package com.darren.cloud.common.transaction;

/**
 * 事务监听器接口
 *
 * @author darren.ouyang
 * @version 2018/9/29 15:18
 */
public interface TransactionListener {

    /**
     * 开始事务监听
     *
     * @param transaction 事务对象
     */
    void onStart (TransactionObject transaction);

    /**
     * 发送事件监听
     *
     * @param transaction 事务对象
     * @param event 事件对象
     */
    void onSendEvent(TransactionObject transaction, TransactionEventObject event);

    /**
     * 提交事务监听
     *
     * @param executor 执行器对象
     */
    void onCommit(TransactionExecutor executor);

    /**
     * 执行开始监听
     *
     * @param executor 执行器对象
     */
    void onExecuteStart(TransactionExecutor executor);

    /**
     * 执行事件后监听
     *
     * @param executor 执行器对象
     * @param event 事件对象
     * @param result 执行结果
     */
    void onExecuteEvent (TransactionExecutor executor, TransactionEventObject event, EventResult result);
}
