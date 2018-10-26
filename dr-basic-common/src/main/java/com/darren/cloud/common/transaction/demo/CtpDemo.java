package com.darren.cloud.common.transaction.demo;

import com.darren.cloud.common.transaction.EventResult;
import com.darren.cloud.common.transaction.TransactionEventModel;
import com.darren.cloud.common.transaction.TransactionFactory;
import com.darren.cloud.common.transaction.schema.ctp.BaseCtpEvent;
import com.darren.cloud.common.transaction.schema.ctp.CtpExecutor;

/**
 * 补偿型事务demo
 *
 * @author darren.ouyang
 * @version 2018/10/22 17:24
 */
public class CtpDemo {

    public static class CtpProducter{

        public void test() {
            CtpExecutor executor = TransactionFactory.createCtpExecutor();
            executor.start();
            EventResult<Long> firstEvent  = executor.executeEvent(new DemoCtpEvent(1));
            EventResult<Long> secondEvent = executor.executeEvent(new DemoCtpEvent(firstEvent.getContent()));
            EventResult<Long> thirdEvent  = executor.executeEvent(new DemoCtpEvent(secondEvent.getContent()));
            EventResult<Long> fourthEvent = executor.executeEvent(new DemoCtpEvent(0));
            EventResult<Long> fiveEvent   = executor.executeEvent(new DemoCtpEvent(firstEvent.getContent()));
            executor.commitSuccess();
        }
    }

    public static class CtpConsumer{
        public static void main1(String[] args) {

        }
    }

    public static class DemoCtpEvent extends BaseCtpEvent {

        private long value;

        public DemoCtpEvent (long value){
            this.value = value;
        }

        @Override
        protected EventResult<Object> executeEvent(TransactionEventModel model) {

            return EventResult.ok(new Object());
        }

        @Override
        protected EventResult<Boolean> compensateEvent(TransactionEventModel model) {

            return EventResult.ok(true);
        }
    }

}
