package com.darren.cloud.common.transaction;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 事务对象
 *
 * @author darren.ouyang
 * @version 2018/9/26 16:38
 */
@Data
@Accessors(chain = true)
public class TransactionObject {

    /**
     * 事务uuid
     */
    private String uuid;

    /**
     * 事务状态
     * @see com.darren.cloud.common.transaction.TransactionStatusEnum
     */
    private String transactionStatus;

    /**
     * 事务备注
     */
    private String remark;

    /**
     * 错误异常信息
     */
    private String errorInfo;

}
