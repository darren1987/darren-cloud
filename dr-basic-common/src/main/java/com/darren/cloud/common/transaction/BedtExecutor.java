package com.darren.cloud.common.transaction;

import static com.darren.cloud.common.transaction.TransactionEventStatusEnum.FAIL;
import static com.darren.cloud.common.transaction.TransactionEventStatusEnum.SUCCEED;
import static com.darren.cloud.common.transaction.TransactionEventStatusEnum.UNKNOWN;
import static com.darren.cloud.common.transaction.TransactionStatusEnum.COMMIT;
import static com.darren.cloud.common.transaction.TransactionStatusEnum.ERROR;
import static com.darren.cloud.common.transaction.TransactionStatusEnum.FINISH;

import com.darren.cloud.common.transaction.data.TransactionDataProvider;
import com.darren.cloud.common.utils.JsonUtils;
import java.util.List;
import java.util.concurrent.ExecutorService;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 最大努力型分布式事务执行器
 * Best Effort Distributed Transaction
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:10
 */
@Data
@Accessors(chain = true)
public class BedtExecutor {

    /**
     * 事务对象
     */
    private TransactionObject transaction;

    /**
     * 事件容器
     */
    private List<TransactionEventObject> eventContainer;

    /**
     * 数据存储供应对象
     */
    private TransactionDataProvider dataProvider;

    /**
     * 监听器
     */
    private BedtListener listener;

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
        // 监听器处理
        listener.onStart(this.transaction);
    }

    /**
     * 发送事务事件
     *
     * @param eventFunction 事件执行器
     */
    public void sendEvent(BaseTransactionEvent eventFunction){
        TransactionEventObject event = new TransactionEventObject()
            .setUuid(this.transaction.getUuid())
            .setNumber(this.eventContainer.size())
            .setEventType(eventFunction.getClass().getSimpleName())
            .setEventStatus(UNKNOWN.getCode())
            .setRetryCount(0)
            .setEventJson(JsonUtils.toJson(eventFunction));

        // 添加事务事件
        this.eventContainer.add(event);

        // 监听器处理
        listener.onSendEvent(this.transaction, event);
    }

    /**
     * 提交事务
     */
    public void commit(){
        String status = COMMIT.getCode();
        transaction.setTransactionStatus(status);
        dataProvider.save(transaction, eventContainer);

        // 监听器处理
        listener.onCommit(transaction, eventContainer);

        // 异步提交事务所有事件默认执行.
        executorService.submit(this::executeTransaction);
    }

    /**
     * 执行事务内所有事件
     */
    void executeTransaction (){
        // 只执行在提交事务
        if (!COMMIT.equalsCode(transaction.getTransactionStatus())){
            return;
        }

        // 开始执行事务监听
        listener.onExecuteBefore(transaction, eventContainer);

        StringBuilder errorInfo = new StringBuilder();
        boolean allEventSucceedFlag = true;
        boolean failFlag = false;
        for (TransactionEventObject event : eventContainer){

            // 过滤已成功的事件
            if (SUCCEED.equalsCode(event.getEventStatus())){
                continue;
            }

            // 事件执行
            BaseTransactionEvent eventFunction = TransactionEventManager.createEvent(event);
            EventResult result = eventFunction.execute0();
            eventResultHandle(event, result);

            if (result.getStatus() != SUCCEED){
                allEventSucceedFlag = false;
                errorInfo.append("事件[").append(event.getNumber()).append("-").append(event.getEventType())
                    .append("] 执行异常: \r\n").append(result.getErrorInfo()).append("\r\n");
                if (result.getStatus() == FAIL){
                    failFlag = true;
                }
            }

            // 开始执行事务监听
            listener.onExecuteEvent(transaction, event, result);
        }

        if (allEventSucceedFlag){
            // 更新事务为完成状态
            dataProvider.updateTransaction(transaction
                .setTransactionStatus(FINISH.getCode())
                .setErrorInfo(errorInfo.toString())
            );

            // 执行最终完成事件
            listener.onExecuteFinish(transaction, eventContainer);
        } else {
            // 如存在最终失败的事件, 标记事务失败, 否则只记录错误状态
            if (!failFlag){
                dataProvider.updateTransaction(transaction.setErrorInfo(errorInfo.toString()));
            } else {
                dataProvider.updateTransaction(transaction
                    .setTransactionStatus(ERROR.getCode())
                    .setErrorInfo(errorInfo.toString())
                );

                // 执行最终失败事件
                listener.onExecuteError(transaction, eventContainer);
            }
        }
    }

    /**
     * 事件结果处理
     *
     * @param event 事件对象
     * @param result 结果
     */
    private void eventResultHandle (TransactionEventObject event, EventResult result){
        // 当前重试次数
        int currentRetryCount = event.getRetryCount() + 1;

        // 本次执行成功
        if (SUCCEED == result.getStatus()){
            dataProvider.updateEvent(event
                .setRetryCount(currentRetryCount)
                .setEventStatus(SUCCEED.getCode())
            );
            return;
        }

        // 超过最大重试次数
        if (maxRetryCount <= currentRetryCount){
            // 更新当前事件状态为失败
            dataProvider.updateEvent(event
                .setEventStatus(FAIL.getCode())
                .setErrorInfo(result.getErrorInfo())
                .setRetryCount(currentRetryCount)
            );
        } else {
            // 更新当前事件重试次数与错误信息
            dataProvider.updateEvent(event
                .setEventStatus(UNKNOWN.getCode())
                .setErrorInfo(result.getErrorInfo())
                .setRetryCount(currentRetryCount)
            );
        }
    }

}
