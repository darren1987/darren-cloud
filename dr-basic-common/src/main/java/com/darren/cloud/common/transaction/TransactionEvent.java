package com.darren.cloud.common.transaction;

/**
 * 事务事件
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:31
 */
public abstract class TransactionEvent {

    /**
     * 执行任务统一入口
     */
    final EventResult execute0 (){
        try{
            return execute();
        } catch (Throwable throwable){
            EventResult result = new EventResult();
            result.setStatus(TransactionEventStatusEnum.FAIL);
            result.setThrowable(throwable);
            return result;
        }
    }

    /**
     * 执行方法
     */
    protected abstract EventResult execute();
}
