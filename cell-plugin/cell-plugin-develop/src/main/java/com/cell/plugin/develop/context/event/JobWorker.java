package com.cell.plugin.develop.context.event;

import com.cell.base.common.events.IEvent;
import com.cell.sdk.log.LOG;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

public interface JobWorker extends InitializingBean {
    @Subscribe
    @Transactional
    default void handleJob(IEvent event)
    {
        if (!predict(event)) {
            return;
        }
        LOG.info("开始处理job:{}", event);
        this.doExecuteJob(event);
    }

    Object doExecuteJob(IEvent event);

    boolean predict(IEvent event);

    @Override
    default void afterPropertiesSet() throws Exception
    {
        JobCenter.getInstance().registerSubscriber(this);
    }
}
