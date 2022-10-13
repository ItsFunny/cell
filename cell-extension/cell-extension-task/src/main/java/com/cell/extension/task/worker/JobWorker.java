package com.cell.extension.task.worker;

import com.cell.base.common.events.IEvent;
import com.cell.base.common.models.Module;
import com.cell.extension.task.event.JobCenter;
import com.cell.sdk.log.LOG;
import com.google.common.eventbus.Subscribe;

public interface JobWorker
{
    @Subscribe
    default void handleJob(IEvent event)
    {
        if (!predict(event))
        {
            return;
        }
        LOG.info(Module.WORKER, "开始处理job:{}", event);
        this.doExecuteJob(event);
    }

    void doExecuteJob(IEvent event);

    boolean predict(IEvent event);

}
