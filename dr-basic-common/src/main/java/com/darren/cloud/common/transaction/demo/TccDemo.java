package com.darren.cloud.common.transaction.demo;

import com.darren.cloud.common.transaction.EventResult;
import com.darren.cloud.common.transaction.TransactionEventModel;
import com.darren.cloud.common.transaction.TransactionFactory;
import com.darren.cloud.common.transaction.schema.tcc.BaseTccEvent;
import com.darren.cloud.common.transaction.schema.tcc.TccExecutor;

/**
 * tcc事务 demo
 *
 * @author darren.ouyang
 * @version 2018/10/22 17:25
 */
public class TccDemo {

    public static class TccProducter{

        public void test() {
            TccExecutor executor = TransactionFactory.createTccExecutor();
            executor.start();
            EventResult<Long> firstEvent  = executor.tryEvent(new DemoTccEvent(1));
            EventResult<Long> secondEvent = executor.tryEvent(new DemoTccEvent(firstEvent.getContent()));
            EventResult<Long> thirdEvent  = executor.tryEvent(new DemoTccEvent(secondEvent.getContent()));
            EventResult<Long> fourthEvent = executor.tryEvent(new DemoTccEvent(0));
            EventResult<Long> fiveEvent   = executor.tryEvent(new DemoTccEvent(firstEvent.getContent()));
            executor.commitConfirm();
        }
    }

    public static class TccConsumer{
        public static void main1(String[] args) {

        }
    }

    public static class DemoTccEvent extends BaseTccEvent {

        private long value;

        public DemoTccEvent (long value){
            this.value = value;
        }

        /**
         * 尝试事件
         */
        @Override
        protected EventResult<Long> tryEvent (TransactionEventModel model){

           return EventResult.ok(1000L);
        }

        /**
         * 提交事件
         */
        @Override
        protected EventResult<Boolean> confirmEvent (TransactionEventModel model){
            return EventResult.ok(true);
        }

        /**
         * 取消事件
         */
        @Override
        protected EventResult<Boolean> cancelEvent (TransactionEventModel model){
            return EventResult.ok(true);
        }
    }
}
