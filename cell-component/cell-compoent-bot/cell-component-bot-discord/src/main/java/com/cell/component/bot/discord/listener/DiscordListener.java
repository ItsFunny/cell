package com.cell.component.bot.discord.listener;

import com.cell.component.bot.discord.handler.DiscordBotMessageManager;
import com.cell.component.bot.discord.handler.DiscordWrapper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        DiscordWrapper wrapper = new DiscordWrapper();
        wrapper.setEvent(event);
        DiscordBotMessageManager.getInstance().execute(wrapper).subscribe();
    }
}
