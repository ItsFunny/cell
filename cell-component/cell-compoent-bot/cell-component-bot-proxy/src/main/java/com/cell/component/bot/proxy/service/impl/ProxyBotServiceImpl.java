package com.cell.component.bot.proxy.service.impl;

import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.core.timewheel.ITimeWheelTaskExecutor;
import com.cell.component.cache.service.ICacheService;
import com.cell.component.http.exception.exception.BusinessException;
import com.cell.component.http.exception.exception.ErrorConstant;
import com.cell.component.http.exception.exception.WrapContextException;
import com.cell.node.core.aop.ArgumentAnnotation;
import com.cell.node.core.context.CellContext;
import com.cell.component.bot.common.IBotService;
import com.cell.component.bot.common.bo.BotType;
import com.cell.component.bot.common.bo.MemberExistReqBO;
import com.cell.component.bot.common.bo.MemberExistRespBO;
import com.cell.component.bot.proxy.service.IProxyBot;

import java.util.List;

public class ProxyBotServiceImpl implements IProxyBot
{
    private List<IBotService> bots;
    private EventLoopGroup eventExecutors;


    public ProxyBotServiceImpl(EventLoopGroup eventExecutors)
    {
        this.eventExecutors = eventExecutors;
    }

    @Override
    public void setBots(List<IBotService> bots)
    {
        this.bots = bots;
    }

    @Override
    public void start()
    {
        for (IBotService bot : bots)
        {
            bot.start();
        }
    }

    @Override
    public void setTaskExecutor(ITimeWheelTaskExecutor ex)
    {
        throw new RuntimeException("asd");
    }

    @Override
    public void setCacheService(ICacheService<String, String> cacheService)
    {
        throw new RuntimeException("asd");
    }

    @Override
    public int getType()
    {
        throw new RuntimeException("asd");
    }

    @Override
    @ArgumentAnnotation
    public MemberExistRespBO exist(CellContext cellContext, MemberExistReqBO req)
    {
        return this.getBotByType(cellContext, req).exist(cellContext, req);
    }

    private IBotService getBotByType(CellContext cellContext, BotType typeF)
    {
        for (IBotService bot : bots)
        {
            if (bot.getType() == typeF.getType())
            {
                return bot;
            }
        }
        throw new WrapContextException(cellContext, new BusinessException(ErrorConstant.ARGUMENT_ILLEGAL));
    }
}
