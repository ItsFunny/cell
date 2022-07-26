package com.cell.component.bot.telegram;

import com.cell.base.core.annotations.Plugin;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.spring.app.SpringNodeAPP;
import com.cell.timewheel.DefaultHashedTimeWheel;
import com.cell.component.bot.telegram.config.TelegramConfig;
import com.cell.component.bot.telegram.service2.impl.TelegramBotServiceImpl;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.component.bot.telegram.service2.ITelegramBotService;

public class TelegramBotExtension extends AbstractSpringNodeExtension
{
    private ITelegramBotService telegramBotService;

    @Plugin
    public ITelegramBotService botService()
    {
        return this.telegramBotService;
    }

    @Override
    protected void onInit(INodeContext iNodeContext) throws Exception
    {
        TelegramConfig.getInstance().init(iNodeContext);
        this.telegramBotService = new TelegramBotServiceImpl(iNodeContext.getEventLoopGroup());
    }

    @Override
    protected void onStart(INodeContext iNodeContext) throws Exception
    {
        SpringNodeAPP app = (SpringNodeAPP) iNodeContext.getApp();
        DefaultHashedTimeWheel wheel = DefaultHashedTimeWheel.getInstance();
        ICacheService bean = app.getAppContext().getBean(ICacheService.class);
        this.telegramBotService.setTaskExecutor(wheel);
        this.telegramBotService.setCacheService(bean);
    }

    @Override
    protected void onReady(INodeContext iNodeContext) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext iNodeContext) throws Exception
    {

    }
}
