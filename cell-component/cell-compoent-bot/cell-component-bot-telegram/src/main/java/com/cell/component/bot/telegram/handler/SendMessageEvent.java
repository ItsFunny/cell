package com.cell.component.bot.telegram.handler;

import lombok.Data;

@Data
public class SendMessageEvent implements IMessage
{
//    private TelegramBotChainInfo info;
    private String msg;

    @Override
    public void discard()
    {

    }

}
