package com.darren.cloud.common.transaction.demo;

import com.darren.cloud.common.transaction.EventResult;
import com.darren.cloud.common.transaction.TransactionEventModel;
import com.darren.cloud.common.transaction.TransactionFactory;
import com.darren.cloud.common.transaction.schema.bedt.BaseBedtEvent;
import com.darren.cloud.common.transaction.schema.bedt.BedtExecutor;
import java.beans.Transient;

/**
 * 最大努力型事务Demo
 *
 * @author darren.ouyang
 * @version 2018/10/22 17:25
 */
public class BedtDemo {
    public static class BedtProducter{

        public Long localBusiness (){
            System.out.println("执行本地主体业务. 返回存储的ID.");
            return 1001L;
        }

        @Transient
        public void test1() {
            Long businessId = localBusiness();
            BedtExecutor executor = TransactionFactory.createBedtExecutor();
            executor.start();
            executor.sendEvent(new DemoBedtEvent(businessId));
            executor.sendEvent(new DemoBedtEvent(businessId + 2));
            executor.sendEvent(new DemoBedtEvent(businessId + 3));
            executor.sendEvent(new DemoBedtEvent(businessId));
            executor.commit();
        }
    }

    public static class BedtConsumer{
        public static void main1(String[] args) {

        }
    }

    public static class DemoBedtEvent extends BaseBedtEvent {

        private long value;

        public DemoBedtEvent (long value){
            this.value = value;
        }

        @Override
        protected EventResult<Boolean> executeEvent(TransactionEventModel model) {

            return EventResult.ok(true);
        }

        @Override
        public String eventType() {
            return DemoBedtEvent.class.getSimpleName();
        }
    }
}
