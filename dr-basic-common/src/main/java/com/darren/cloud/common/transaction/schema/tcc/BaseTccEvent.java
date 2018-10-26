package com.darren.cloud.common.transaction.schema.tcc;

import com.darren.cloud.common.transaction.EventResult;
import com.darren.cloud.common.transaction.TransactionEvent;
import com.darren.cloud.common.transaction.TransactionEventModel;

/**
 * tcc事务事件
 *
 * @author darren.ouyang
 * @version 2018/10/22 15:26
 */
public abstract class BaseTccEvent implements TransactionEvent {

    @Override
    public String eventType() {
        return getClass().getSimpleName();
    }

    /**
     * 事件尝试方法
     *
     * @param model 事件Model
     * @return 执行结果
     */
    protected abstract EventResult tryEvent (TransactionEventModel model);

    /**
     * 事件提交方法
     *
     * @param model 事件Model
     * @return 执行结果
     */
    protected abstract EventResult<Boolean> confirmEvent (TransactionEventModel model);

    /**
     * 事件取消方法
     *
     * @param model 事件Model
     * @return 执行结果
     */
    protected abstract EventResult<Boolean> cancelEvent (TransactionEventModel model);
}
