package com.darren.cloud.common.transaction;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 事务事件
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:31
 */
public abstract class BaseTransactionEvent {

    /**
     * 执行任务统一入口
     */
    final EventResult execute0 (){
        try{
            EventResult result = execute();
            if(TransactionEventStatusEnum.FAIL == result.getStatus() && result.getThrowable() != null){
                String errorInfo = result.getErrorInfo() == null ? "" : result.getErrorInfo();
                result.setErrorInfo(errorInfo + "\r\n" + ExceptionUtils.getStackTrace(result.getThrowable()));
            }
            return result ;
        } catch (Throwable throwable){

            // 执行抛出异常
            EventResult result = new EventResult();
            result.setStatus(TransactionEventStatusEnum.FAIL);
            result.setThrowable(throwable);

            String errorInfo = result.getErrorInfo() == null ? "" : result.getErrorInfo();
            result.setErrorInfo(errorInfo + "\r\n" + ExceptionUtils.getStackTrace(throwable));
            return result;
        }
    }

    /**
     * 执行方法
     *
     * @return 执行结果
     */
    protected abstract EventResult execute();
}
