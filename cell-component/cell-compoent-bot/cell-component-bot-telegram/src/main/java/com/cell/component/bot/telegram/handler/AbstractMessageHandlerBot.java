package com.cell.component.bot.telegram.handler;

import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

public abstract class AbstractMessageHandlerBot extends TelegramLongPollingBot implements IReactorExecutor<IMessage>
{
    private String name;
    private String token;

    public AbstractMessageHandlerBot(String name, String token)
    {
        this.name = name;
        this.token = token;
    }

    @Override
    public String getBotUsername()
    {
        return this.name;
    }

    @Override
    public String getBotToken()
    {
        return this.token;
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        EventWrapper wrapper = new EventWrapper();
        wrapper.setHandler(this);
        wrapper.setUpdate(update);
        if (update.hasMessage() && update.getMessage().hasText() && (update.getMessage().getText().contains("@")))
        {
            String[] split = update.getMessage().getText().split("@");
            update.getMessage().setText(split[0]);
        }
        MsgExecutorManager.getInstance().execute(wrapper).subscribe();
    }


    @Override
    public Mono<Void> execute(IMessage iMessage, IChainExecutor<IMessage> iChainExecutor)
    {
        this.handleMsg(iMessage);
        return iChainExecutor.execute(iMessage);
    }

    @Override
    public boolean predict(IMessage iMessage)
    {
        return this.doPredict(iMessage);
    }


    protected abstract boolean doPredict(IMessage message);

    protected abstract void handleMsg(IMessage msg);
}
