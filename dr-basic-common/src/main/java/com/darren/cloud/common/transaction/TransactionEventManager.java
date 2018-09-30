package com.darren.cloud.common.transaction;

import com.darren.cloud.common.utils.JsonUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 事务管理器
 *
 * @author darren.ouyang
 * @version 2018/9/26 19:36
 */
public final class TransactionEventManager {

    /**
     * 事件class对象容器 map《事件类名, 事件class对象》
     */
    private final static Map<String, Class<? extends BaseTransactionEvent>> eventClassMap = new HashMap<>();

    /**
     * 注册事件
     *
     * @param clazz 事件class对象
     */
    public synchronized static void registerEvent (Class<? extends BaseTransactionEvent> clazz){
        String simpleName = clazz.getSimpleName();
        if (eventClassMap.containsKey(simpleName)){
            throw new RuntimeException(MessageFormat.format(
                "register transaction event register error, event[{0}] already exist, all events：{1}",
                simpleName, eventClassMap.keySet()));
        }

        eventClassMap.put(simpleName, clazz);
    }

    /**
     * 创建事件
     *
     * @param event 事件对象
     * @return 事件
     */
    public static BaseTransactionEvent createEvent (TransactionEventObject event){
        String simpleName = event.getEventType();
        Class<? extends BaseTransactionEvent> clazz = eventClassMap.get(event.getEventType());
        if (clazz == null){
            throw new RuntimeException(MessageFormat.format(
                "register transaction event create error, event[{0}] not exist, all events：{1}",
                simpleName, eventClassMap.keySet()));
        }

        return JsonUtils.jsonToType(event.getEventJson(), clazz);
    }

}
