package com.cell.component.bot.telegram.config;

import com.cell.base.common.decorators.IDecorator;
import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TelegramConfig
{
    public static final String telegramBotModule = "telegramBot";

    private static TelegramConfig instance = new TelegramConfig();

    private List<TelegramNode> nodes;
    private List<String> ignoreMsg = new ArrayList<>();


    private TelegramConfig()
    {
        this.ignoreMsg.add("token is not exists");
    }

    public static TelegramConfig getInstance()
    {
        return instance;
    }

    @Data
    public static class TelegramNode
    {
        private String name;
        private String token;
    }

    public void init(INodeContext context)
    {
        TelegramConfig inst = Configuration.getDefault().getAndMonitorConfig(telegramBotModule, TelegramConfig.class, (cfg) ->
        {
            instance = cfg;
        });
        instance = inst;
    }

    private static final String template = "https://t.me/%s";

    public String getMeUrl(IDecorator<String> decorator)
    {
        TelegramNode telegramNode = this.nodes.get(0);
        String name = telegramNode.getName();
        return decorator.decorate(String.format(template, name));
    }
}
