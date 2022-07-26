package com.cell.component.bot.telegram.handler;

public class SendMsgBot extends AbstractMessageHandlerBot
{
    public SendMsgBot(String name, String token)
    {
        super(name, token);
    }

    @Override
    protected boolean doPredict(IMessage message)
    {
        if (!(message instanceof SendMessageEvent))
        {
            return false;
        }
        return true;
//        SendMessageEvent event = (SendMessageEvent) message;
//        return event.getInfo().getBotName().equalsIgnoreCase(this.getBotUsername());
    }

    @Override
    protected void handleMsg(IMessage msg)
    {
//        SendMessageEvent event = (SendMessageEvent) msg;
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText(event.getMsg());
//        sendMessage.setChatId(event.getInfo().getChainId());
//        try
//        {
//            this.execute(sendMessage);
//            LOG.info(TelegramModule.TELEGRAM, "send msg:{},botName:{}", event.getMsg(), this.getBotUsername());
//        } catch (TelegramApiException e)
//        {
//            LOG.error(TelegramModule.TELEGRAM, e, "send msg failed");
//        }
    }
}
