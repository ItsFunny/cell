package com.cell.component.bot.twitter;

import com.cell.base.core.annotations.Plugin;
import com.cell.component.bot.twitter.config.TwitterBotConfig;
import com.cell.component.bot.twitter.service2.ITwitterBotService;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.app.SpringNodeAPP;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.timewheel.DefaultHashedTimeWheel;
import com.cell.component.bot.twitter.service2.impl.TwitterBotServiceImpl;

public class TwitterBotExtension extends AbstractSpringNodeExtension
{

    private ITwitterBotService twitterBotService;

    @Plugin
    public ITwitterBotService twitterBotService()
    {
        return this.twitterBotService;
    }

    @Override
    protected void onInit(INodeContext iNodeContext) throws Exception
    {
        TwitterBotConfig.seal();
        this.twitterBotService = new TwitterBotServiceImpl(iNodeContext.getEventLoopGroup());
    }

    @Override
    protected void onStart(INodeContext iNodeContext) throws Exception
    {
        SpringNodeAPP app = (SpringNodeAPP) iNodeContext.getApp();
        DefaultHashedTimeWheel wheel = DefaultHashedTimeWheel.getInstance();
        ICacheService bean = app.getAppContext().getBean(ICacheService.class);
        this.twitterBotService.setTaskExecutor(wheel);
        this.twitterBotService.setCacheService(bean);
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
