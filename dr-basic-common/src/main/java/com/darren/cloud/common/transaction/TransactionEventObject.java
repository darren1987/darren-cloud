package com.darren.cloud.common.transaction;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 事务事件实体
 *
 * @author darren.ouyang
 * @version 2018/9/26 16:39
 */
@Data
@Accessors(chain = true)
public class TransactionEventObject {

    /**
     * 事务uuid
     */
    private String uuid;

    /**
     * 事务执行编号
     */
    private Integer number;

    /**
     * 事件类型
     * @see com.darren.cloud.common.transaction.TransactionEventManager#eventClassMap
     */
    private String eventType;

    /**
     * 事件状态
     * @see com.darren.cloud.common.transaction.TransactionEventStatusEnum
     */
    private String eventStatus;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 错误异常信息
     */
    private String errorInfo;

    /**
     * 事件json数据
     */
    private String eventJson;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof TransactionEventObject)) {
            return false;
        }
        TransactionEventObject object = (TransactionEventObject) o;
        return Objects.equals(object.uuid, uuid)
            && Objects.equals(object.number, number)
            && Objects.equals(object.eventType, eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, number, eventType);
    }

    /**
     * 事件唯一标识
     * 规则: 事务uuid-执行编号-事件类型
     *
     * @return 获的一个事件唯一标识
     */
    public String uniqueKey (){
        return MessageFormat.format("{0}-{1}-{2}", uuid, number, eventType);
    }
}
