package com.cell.component.bot.proxy;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.core.annotations.CellOrder;
import com.cell.component.bot.proxy.service.IProxyBot;
import com.cell.component.bot.proxy.service.impl.ProxyBotServiceImpl;
import com.cell.component.cache.service.ICacheService;
import com.cell.component.cache.service.impl.DefaultMemoryCacheServiceImpl;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.timewheel.DefaultHashedTimeWheel;
import com.cell.component.bot.common.IBotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;

@CellOrder(value = OrderConstants.MAX_ORDER)
public class ProxyBotExtension extends AbstractSpringNodeExtension
{
    private IProxyBot proxyBot;

    private ICacheService<String, String> cacheService;

    @Bean
    public ICacheService cacheService()
    {
        return this.cacheService;
    }

    @Bean
    @Primary
    public IProxyBot proxyBot(List<IBotService> botServices)
    {
        this.proxyBot.setBots(botServices);
        return this.proxyBot;
    }

    @Override
    protected void onInit(INodeContext iNodeContext) throws Exception
    {
        this.proxyBot = new ProxyBotServiceImpl(iNodeContext.getEventLoopGroup());
        this.cacheService = new DefaultMemoryCacheServiceImpl<>(DefaultHashedTimeWheel.getInstance());
    }

    @Override
    protected void onStart(INodeContext iNodeContext) throws Exception
    {
        this.proxyBot.start();
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
