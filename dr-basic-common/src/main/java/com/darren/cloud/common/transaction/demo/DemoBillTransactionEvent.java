package com.darren.cloud.common.transaction.demo;

import com.darren.cloud.common.transaction.EventResult;
import com.darren.cloud.common.transaction.BaseTransactionEvent;
import com.darren.cloud.common.transaction.TransactionEventManager;
import com.darren.cloud.common.transaction.demo.DemoProducerServer.DemoOrder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 店长分账事件
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:37
 */
@Data
@Accessors(chain = true)
public class DemoBillTransactionEvent extends BaseTransactionEvent {

    private String user;            // 店长
    private DemoOrder sourceOrder;  // 来源订单实体

    // 注册当前事件
    static {
        TransactionEventManager.registerEvent(DemoBillTransactionEvent.class);
    }

    /**
     * 创建事件
     *
     * @param user 店长
     * @param sourceOrder 来源订单实体
     * @return 事件对象
     */
    public static DemoBillTransactionEvent of (String user, DemoOrder sourceOrder){
        DemoBillTransactionEvent event = new DemoBillTransactionEvent();
        event.user = user;
        event.sourceOrder = sourceOrder;
        return event;
    }


    @Override
    protected EventResult execute() {
        /*
        final MarketingFeignClient marketingFeignClient = SpringContextUtils.getBean(MarketingFeignClient.class);

        ResponseEntity<Boolean> billResponse = marketingFeignClient.returnedOrderBill(tenantCode, refundBill);
        FeignUtils.validateResult(billResponse, "退货单完成退款失败, 退款账单核算异常.{0}, {1}",
            JsonUtils.toJson(refundBill), JsonUtils.toJson(billResponse));
       */

        if ((++count) % 5 == 0){
            System.out.println("事件执行成功: 店长分账事件. count: " + count);
            return EventResult.ok();
        } else if (count % 3 == 0) {
            throw new RuntimeException("事件执行失败: 店长分账事件. 出现了异常: " + count);
        } else {
            System.out.println("事件执行失败: 店长分账事件. count: " + count);
            return EventResult.error().setErrorInfo("事件执行失败: 店长分账事件. 次数还未累加到5");
        }
    }

    private static int count = 0;
}
