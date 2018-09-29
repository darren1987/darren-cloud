package com.darren.cloud.common.transaction;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 事件执行结果集合
 *
 * @author darren.ouyang
 * @version 2018/9/29 15:38
 */
@Data
@Accessors(chain = true)
public class EventResult {

    /**
     * 执行状态
     */
    private TransactionEventStatusEnum status;

    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 截获异常
     */
    private Throwable throwable;

    /**
     * 执行成功
     *
     * @return 结果
     */
    public static EventResult ok(){
        return new EventResult().setStatus(TransactionEventStatusEnum.SUCCEED);
    }

    /**
     * 执行失败
     *
     * @return 结果
     */
    public static EventResult error(){
        return new EventResult().setStatus(TransactionEventStatusEnum.FAIL);
    }
}
