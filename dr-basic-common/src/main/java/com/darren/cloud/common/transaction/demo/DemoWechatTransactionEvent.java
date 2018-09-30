package com.darren.cloud.common.transaction.demo;

import com.darren.cloud.common.transaction.EventResult;
import com.darren.cloud.common.transaction.BaseTransactionEvent;
import com.darren.cloud.common.transaction.TransactionEventManager;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通知微信用户事件
 *
 * @author darren.ouyang
 * @version 2018/9/29 14:42
 */
@Data
@Accessors(chain = true)
public class DemoWechatTransactionEvent extends BaseTransactionEvent {

    private String user;            // 微信用户
    private String orderName;       // 订单名称

    // 注册当前事件
    static {
        TransactionEventManager.registerEvent(DemoWechatTransactionEvent.class);
    }

    /**
     * 创建事件
     *
     * @param user 店长
     * @param orderName 订单名称
     * @return 事件对象
     */
    public static DemoWechatTransactionEvent of (String user, String orderName){
        DemoWechatTransactionEvent event = new DemoWechatTransactionEvent();
        event.user = user;
        event.orderName = orderName;
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

        System.out.println("事件执行: 通知微信用户事件. orderName: " + orderName);
        return EventResult.ok();
    }
}
