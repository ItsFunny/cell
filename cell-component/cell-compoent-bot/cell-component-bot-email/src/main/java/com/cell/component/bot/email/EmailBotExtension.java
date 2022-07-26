package com.cell.component.bot.email;

import com.cell.base.core.annotations.Plugin;
import com.cell.component.bot.email.service2.IEmailBotService;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.component.bot.email.config.EmailBotConfig;
import com.cell.component.bot.email.service2.impl.EmailBotServiceImpl;

public class EmailBotExtension extends AbstractSpringNodeExtension
{
    private IEmailBotService botService;

    @Plugin
    public IEmailBotService botService()
    {
        return this.botService;
    }

    @Override
    protected void onInit(INodeContext iNodeContext) throws Exception
    {
        this.botService = new EmailBotServiceImpl(iNodeContext.getEventLoopGroup());
        EmailBotConfig.getInstance().seal(iNodeContext);
    }

    @Override
    protected void onStart(INodeContext iNodeContext) throws Exception
    {

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
