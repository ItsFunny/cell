package com.cell.component.bot.common;

import com.cell.base.common.models.ModuleInterface;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.core.timewheel.ITimeWheelTaskExecutor;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.CellContext;
import com.cell.component.bot.common.bo.MemberExistReqBO;
import com.cell.component.bot.common.bo.MemberExistRespBO;

public abstract class AbstractBotService implements IBotService
{
    protected ModuleInterface module;
    protected EventLoopGroup eventExecutors;
    protected ICacheService<String, String> cacheService;
    protected ITimeWheelTaskExecutor executor;

    public AbstractBotService(ModuleInterface module,
                              EventLoopGroup eventExecutors)
    {
        this.module = module;
        this.eventExecutors = eventExecutors;
    }

    @Override
    public void start()
    {
        this.executor.initOnce(null);
        this.onStart();
    }

    @Override
    public void setTaskExecutor(ITimeWheelTaskExecutor ex)
    {
        this.executor = ex;
    }

    @Override
    public void setCacheService(ICacheService<String, String> cacheService)
    {
        this.cacheService = cacheService;
    }

    @Override
    public MemberExistRespBO exist(CellContext cellContext, MemberExistReqBO req)
    {
        return this.onExist(cellContext, req);
    }

    protected abstract MemberExistRespBO onExist(CellContext cellContext, MemberExistReqBO req);

    protected abstract void onStart();
}
