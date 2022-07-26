package com.cell.component.bot.discord.handler;

import com.cell.base.core.annotations.Manager;
import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.plugin.pipeline.manager.IReflectManager;

import java.util.Collection;

@Manager(name = DiscordBotMessageManager.discordBotMessage)
public class DiscordBotMessageManager extends AbstractEventCenter
{
    public static final String discordBotMessage = "discordBotMessage";
    private static final DiscordBotMessageManager instance = new DiscordBotMessageManager();

    public static DiscordBotMessageManager getInstance()
    {
        return instance;
    }

    @Override
    protected void afterInvoke()
    {

    }

    @Override
    public void invokeInterestNodes(Collection<Object> nodes)
    {
        super.invokeInterestNodes(nodes);
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
