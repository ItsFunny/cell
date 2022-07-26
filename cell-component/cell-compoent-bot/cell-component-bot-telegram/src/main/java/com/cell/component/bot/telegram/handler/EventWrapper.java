package com.cell.component.bot.telegram.handler;

import com.cell.base.core.protocol.IContext;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
public class EventWrapper implements IContext
{
    private boolean handled;
    private Update update;
    private AbstractMessageHandlerBot handler;

    @Override
    public void discard()
    {

    }
}
