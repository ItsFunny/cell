package com.cell.hook;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.cell.log.LOG;
import com.cell.models.Module;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-09 10:47
 */
//public class InstanceHooker extends Subscriber<InstancesChangeEvent>
//{
//    @Override
//    public void onEvent(InstancesChangeEvent event)
//    {
//        LOG.info(Module.HTTP_GATEWAY, "收到event:{}", event);
//    }
//
//    @Override
//    public Class<? extends Event> subscribeType()
//    {
//        return InstancesChangeEvent.class;
//    }
//}
