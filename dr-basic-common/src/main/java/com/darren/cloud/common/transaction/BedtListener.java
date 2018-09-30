package com.darren.cloud.common.transaction;

import java.util.List;
import org.apache.logging.log4j.LogManager;

/**
 * 最大努力型分布式事务监听器接口
 *
 * @author darren.ouyang
 * @version 2018/9/29 15:18
 */
public class BedtListener {

    /**
     * 开始事务监听
     *
     * @param transaction 事务对象
     */
    public void onStart (TransactionObject transaction){
        if (LogManager.getLogger().isDebugEnabled()){
            LogManager.getLogger().debug("Bedt事务[{}-{}] 开始事务.\r\n{}",
                transaction.getRemark(), transaction.getUuid(), transaction);
        }
    }

    /**
     * 发送事件监听
     *
     * @param transaction 事务对象
     * @param event 事件对象
     */
    public void onSendEvent(TransactionObject transaction, TransactionEventObject event){
        if (LogManager.getLogger().isDebugEnabled()){
            LogManager.getLogger().debug("Bedt事务[{}-{}] 发送事件.\r\n{}",
                transaction.getRemark(), transaction.getUuid(), event);
        }

    }

    /**
     * 提交事务监听
     *
     * @param transaction 事务对象
     * @param transaction 事件容器
     */
    public void onCommit(TransactionObject transaction, List<TransactionEventObject> eventContainer){
        if (LogManager.getLogger().isDebugEnabled()){
            LogManager.getLogger().debug("Bedt事务[{}-{}] 提交事务.",
                transaction.getRemark(), transaction.getUuid());
        }
    }

    /**
     * 执行开始监听
     *
     * @param transaction 事务对象
     * @param transaction 事件容器
     */
    public void onExecuteBefore(TransactionObject transaction, List<TransactionEventObject> eventContainer){
        if (LogManager.getLogger().isDebugEnabled()){
            LogManager.getLogger().debug("Bedt事务[{}-{}] 执行开始.",
                transaction.getRemark(), transaction.getUuid());
        }
    }

    /**
     * 执行最终成功
     *
     * @param transaction 事务对象
     * @param transaction 事件容器
     */
    public void onExecuteFinish(TransactionObject transaction, List<TransactionEventObject> eventContainer){
        if (LogManager.getLogger().isDebugEnabled()){
            LogManager.getLogger().debug("Bedt事务[{}-{}] 执行最终成功.",
                transaction.getRemark(), transaction.getUuid());
        }
    }

    /**
     * 执行最终失败,需人工处理监听
     *
     * @param transaction 事务对象
     * @param transaction 事件容器
     */
    public void onExecuteError(TransactionObject transaction, List<TransactionEventObject> eventContainer){
        if (LogManager.getLogger().isDebugEnabled()){
            LogManager.getLogger().debug("Bedt事务[{}-{}] 执行最终失败.\r\n{}",
                transaction.getRemark(), transaction.getUuid(), transaction.getErrorInfo());
        }
    }

    /**
     * 执行事件后监听
     *
     * @param transaction 事务对象
     * @param event 事件对象
     * @param result 执行结果
     */
    public void onExecuteEvent (TransactionObject transaction, TransactionEventObject event, EventResult result){
        if (result.getStatus() != TransactionEventStatusEnum.SUCCEED && LogManager.getLogger().isDebugEnabled()){
            LogManager.getLogger().debug("Bedt事务[{}-{}] 事件[{}-{}]执行失败.\r\n{}",
                transaction.getRemark(), transaction.getUuid(), event.getNumber(), event.getEventType(), event.getErrorInfo());
        }
    }
}
