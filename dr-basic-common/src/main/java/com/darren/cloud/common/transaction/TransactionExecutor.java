package com.darren.cloud.common.transaction;

import com.darren.cloud.common.transaction.data.TransactionDataProvider;
import com.darren.cloud.common.utils.JsonUtils;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 事务执行器
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:10
 */
@Data
@Accessors(chain = true)
public class TransactionExecutor {

    /**
     * 事务对象
     */
    private TransactionObject transaction;

    /**
     * 事件容器
     */
    private Map<TransactionEventObject, TransactionEvent> eventContainer;

    /**
     * 数据存储供应对象
     */
    private TransactionDataProvider dataProvider;

    /**
     * 监听器
     */
    private TransactionListener listener;

    /**
     * 线程池服务
     */
    private ExecutorService executorService;

    /**
     * 扫描重试最大次数
     */
    private int maxRetryCount;

    /**
     * 启动事务
     */
    public void start(){

        // 保存事务对象
        dataProvider.insertTransaction(this.transaction);

        // 监听器处理
        listener.onStart(this.transaction);
    }

    /**
     * 发送事务事件
     *
     * @param eventFunction 事件执行器
     */
    public void sendEvent(TransactionEvent eventFunction){
        TransactionEventObject event = new TransactionEventObject()
            .setUuid(this.transaction.getUuid())
            .setNumber(this.eventContainer.size())
            .setEventType(eventFunction.getClass().getSimpleName())
            .setEventStatus(TransactionEventStatusEnum.UNKNOWN.getCode())
            .setRetryCount(0)
            .setEventJson(JsonUtils.toJson(eventFunction))
            .setUpdateTime(new Date())
            .setCreateTime(new Date());

        // 添加事务事件
        this.eventContainer.put(event, eventFunction);

        // 保存事件对象
        dataProvider.insertEvent(event);

        // 监听器处理
        listener.onSendEvent(this.transaction, event);
    }

    /**
     * 提交事务
     */
    public void commit(){
        String status = TransactionStatusEnum.COMMIT.getCode();
        transaction
            .setTransactionStatus(status)
            .setUpdateTime(new Date());
        dataProvider.updateTransaction(transaction);

        // 异步提交事务所有事件默认执行.
        executorService.submit(this::executeTransaction);

        // 监听器处理
        listener.onCommit(this);
    }

    /**
     * 执行事务内所有事件
     */
    void executeTransaction (){

        // 只执行在提交事务
        if (!TransactionStatusEnum.COMMIT.equalsCode(transaction.getTransactionStatus())){
            return;
        }

        // 开始执行事务监听
        listener.onExecuteStart(this);

        boolean allEventSucceedFlag = true;
        for (Entry<TransactionEventObject, TransactionEvent> entry : eventContainer.entrySet()){
            TransactionEventObject event = entry.getKey();
            TransactionEvent eventFunction = entry.getValue();

            // 过滤已成功的事件
            if (TransactionEventStatusEnum.SUCCEED.equalsCode(event.getEventStatus())){
                continue;
            }

            EventResult result = eventFunction.execute0();  // 事件执行
            eventResultHandle(event, result);               // 执行结果处理

            // 开始执行事务监听
            listener.onExecuteEvent(this, event, result);
        }

        // 全部事件执行成功
        if (allEventSucceedFlag){
            dataProvider.updateTransactionToFinish(
                transaction
                    .setTransactionStatus(TransactionStatusEnum.FINISH.getCode())
                    .setUpdateTime(new Date())
            );
        }
    }


    private void eventResultHandle (TransactionEventObject event, EventResult result){
        // 当前重试次数
        int currentRetryCount = event.getRetryCount() + 1;
        if (TransactionEventStatusEnum.SUCCEED == result.getStatus()){
            // 本次执行成功
            dataProvider.updateEvent(
                event
                    .setRetryCount(currentRetryCount)
                    .setEventStatus(TransactionEventStatusEnum.SUCCEED.getCode())
                    .setUpdateTime(new Date())
            );
        } else {
            //allEventSucceedFlag = false;

            // 超过最大重试次数
            if (maxRetryCount <= currentRetryCount){
                // 更新当前事件状态为失败
                dataProvider.updateEvent(
                    event
                        .setEventStatus(TransactionEventStatusEnum.FAIL.getCode())
                        .setErrorInfo("errorInfo")
                        .setRetryCount(currentRetryCount)
                        .setUpdateTime(new Date())
                );

                // 更新整个事务状态为失败, 需人工介入
                dataProvider.updateTransaction(
                    transaction
                        .setTransactionStatus(TransactionStatusEnum.ERROR.getCode())
                        .setErrorInfo("errorInfo")
                        .setUpdateTime(new Date())
                );
                return;
            } else {
                // 更新当前事件重试次数与错误信息
                dataProvider.updateEvent(
                    event
                        .setErrorInfo("errorInfo")
                        .setRetryCount(currentRetryCount)
                        .setUpdateTime(new Date())
                );
            }
        }
    }

}
