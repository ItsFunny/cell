package com.cell.extension.task;

import com.cell.extension.task.event.CellJobCenter;
import com.cell.extension.task.worker.JobWorker;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.app.SpringNodeAPP;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

public class TaskExtension extends AbstractSpringNodeExtension {

    @Override
    protected void onInit(INodeContext ctx) throws Exception {
        CellJobCenter.getInstance().seal(ctx);
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception {
        SpringNodeAPP app = (SpringNodeAPP) ctx.getApp();
        ConfigurableApplicationContext appContext = app.getAppContext();
        Map<String, JobWorker> workers = appContext.getBeansOfType(JobWorker.class);
        for (String s : workers.keySet()) {
            JobWorker worker = workers.get(s);
            CellJobCenter.getInstance().registerSubscriber(worker);
        }
    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception {

    }
}
