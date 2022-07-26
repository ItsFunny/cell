package com.cell.component.bot.discord;

import com.cell.component.bot.discord.config.DiscordBotConfig;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.app.SpringNodeAPP;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.timewheel.DefaultHashedTimeWheel;
import com.cell.component.bot.discord.service2.IDiscordBotService;
import com.cell.component.bot.discord.service2.impl.DiscordBotServiceImpl;
import org.springframework.context.annotation.Bean;

/*
    1. 需要先获取得到bot的token
    2. 确定bot在哪个server生效
    3. 确定bot在哪个server的哪个channel生效
    总共只需要这3个即可
 */
public class DiscordBotExtension extends AbstractSpringNodeExtension
{
    private IDiscordBotService discordBotService;

    @Bean
    public IDiscordBotService discordBotService()
    {
        return this.discordBotService;
    }

    @Override
    protected void onInit(INodeContext iNodeContext) throws Exception
    {
        DiscordBotConfig.getInstance().seal(iNodeContext);
        this.discordBotService = new DiscordBotServiceImpl(iNodeContext.getEventLoopGroup());
    }

    @Override
    protected void onStart(INodeContext iNodeContext) throws Exception
    {
        SpringNodeAPP app = (SpringNodeAPP) iNodeContext.getApp();
        DefaultHashedTimeWheel wheel = DefaultHashedTimeWheel.getInstance();
        ICacheService bean = app.getAppContext().getBean(ICacheService.class);
        this.discordBotService.setCacheService(bean);
        this.discordBotService.setTaskExecutor(wheel);
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
