
### 本地消息事务中间件

### 待解决问题
- 支持tcc模式
- 消费端的幂等性封装
- 具体实现服务的数据层(mysql)实现
- 事件执行异常的error信息存储

### 解决
- 具体实现服务的数据层(memory)实现

### 备注
- 2PC : Two-phase commit protocol 两阶段提交
- TCC : Try、Commit、Cancel 

```java
public class DemoTransaction {
    @Transactional
    public void demo1 (){
      TransactionExecutor executor = TransactionExecutorFactory.createExecutor("demo");
      executor.start();
      executor.sendEvent(RefundBillTransactionEvent.of("lqx", new OrderRefundBill()));
      executor.sendEvent(RefundBillTransactionEvent.of("lqx", new OrderRefundBill()));
      executor.sendEvent(RefundBillTransactionEvent.of("lqx", new OrderRefundBill()));
      executor.commit(); 
    }
}

```
