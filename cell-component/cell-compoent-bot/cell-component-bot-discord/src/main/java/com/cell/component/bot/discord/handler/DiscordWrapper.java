package com.cell.component.bot.discord.handler;

import com.cell.base.core.protocol.IContext;
import lombok.Data;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Data
public class DiscordWrapper implements IContext
{
    private boolean handled;
    private MessageReceivedEvent event;
}
